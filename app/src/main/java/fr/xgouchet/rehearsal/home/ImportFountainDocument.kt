package fr.xgouchet.rehearsal.home

import android.content.Context
import android.graphics.Color
import android.net.Uri
import fr.xgouchet.fountain.parser.dom.ActionCue
import fr.xgouchet.fountain.parser.dom.CharacterCue
import fr.xgouchet.fountain.parser.dom.FountainDomParser
import fr.xgouchet.fountain.parser.dom.Scene
import fr.xgouchet.fountain.parser.dom.Script
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

class ImportFountainDocument(
        private val context: Context,
        private val uri: Uri
) : SingleOnSubscribe<Boolean> {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    override fun subscribe(emitter: SingleEmitter<Boolean>) {

        try {
            val script = parseScript()

            if (script != null) {
                saveScript(script)

                emitter.onSuccess(true)
            } else {
                emitter.onSuccess(false)
            }
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }


    private fun parseScript(): Script? {
        val parser = FountainDomParser()
        val input = context.contentResolver.openInputStream(uri) ?: return null
        return parser.parse(input)
    }

    private fun saveScript(script: Script) {

        val scriptModel = ScriptModel(
                title = script.titlePage.title
        )

        val scriptId = appDatabase.scriptDao().insert(scriptModel).toInt()
        var position = 0

        script.parts.forEach {
            if (it is Scene) {
                addScene(it, scriptId, position)
                position++
            }
        }

    }

    private fun addScene(scene: Scene,
                         scriptId: Int,
                         position: Int) {
        val sceneModel = SceneModel(
                scriptId = scriptId,
                position = position,
                description = scene.header.description,
                numbering = scene.header.numbering
        )
        val sceneId = appDatabase.sceneDao().insert(sceneModel).toInt()
        var cuePosition = 0
        scene.cues.forEach {
            when (it) {
                is CharacterCue -> {
                    addCharacterCue(it, scriptId, sceneId, cuePosition)
                    cuePosition += it.parts.size
                }
                is ActionCue -> {
                    addActionCue(it, sceneId, cuePosition)
                }
            }

        }
    }

    private fun addActionCue(actionCue: ActionCue, sceneId: Int, cuePosition: Int) {
        val cueModel = CueModel(
                sceneId = sceneId,
                characterId = null,
                type = CueModel.TYPE_ACTION,
                characterExtension = null,
                position = cuePosition,
                content = actionCue.direction
        )
        appDatabase.cueDao().insert(cueModel)
    }

    private fun addCharacterCue(characterCue: CharacterCue,
                                scriptId: Int,
                                sceneId: Int,
                                cuePosition: Int) {
        val existingCharacterModel = appDatabase.characterDao().getByNameInScript(characterCue.characterName, scriptId)

        val characterId = existingCharacterModel?.id ?: appDatabase.characterDao().insert(
                CharacterModel(
                        scriptId = scriptId,
                        name = characterCue.characterName
                )
        ).toInt()

        var partPosition = 0
        characterCue.parts.forEach {
            addCharacterCuePart(it, characterCue.characterExtension, sceneId, characterId, cuePosition + partPosition)
            partPosition++
        }
    }

    private fun addCharacterCuePart(part: CharacterCue.Part,
                                    extension: String?,
                                    sceneId: Int,
                                    characterId: Int,
                                    position: Int) {
        val (type, content) = when (part) {
            is CharacterCue.Dialog -> CueModel.TYPE_DIALOG to part.line
            is CharacterCue.Parenthetical -> CueModel.TYPE_ACTION to part.direction
            else -> CueModel.TYPE_UNKNOWN to ""
        }

        val cueModel = CueModel(
                sceneId = sceneId,
                characterId = characterId,
                type = type,
                characterExtension = extension,
                position = position,
                content = content
        )
        appDatabase.cueDao().insert(cueModel)
    }

}
