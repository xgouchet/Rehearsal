package fr.xgouchet.rehearsal.screen.home

import android.content.Context
import android.net.Uri
import fr.xgouchet.fountain.parser.dom.ActionCue
import fr.xgouchet.fountain.parser.dom.CharacterCue
import fr.xgouchet.fountain.parser.dom.FountainDomParser
import fr.xgouchet.fountain.parser.dom.LyricsCue
import fr.xgouchet.fountain.parser.dom.Scene
import fr.xgouchet.fountain.parser.dom.Script
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.core.room.model.SceneDbModel
import fr.xgouchet.rehearsal.core.room.model.ScriptDbModel
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

        val scriptModel = ScriptDbModel(
                title = script.titlePage.title,
                author = script.titlePage.author
        )

        val scriptId = appDatabase.scriptDao().insert(scriptModel)
        var position = 0

        script.parts.forEach {
            if (it is Scene) {
                addScene(it, scriptId, position)
                position++
            }
        }

    }

    private fun addScene(scene: Scene,
                         scriptId: Long,
                         position: Int) {
        val sceneModel = SceneDbModel(
                scriptId = scriptId,
                position = position,
                description = scene.header.description,
                numbering = scene.header.numbering
        )
        val sceneId = appDatabase.sceneDao().insert(sceneModel)
        var cuePosition = 0
        scene.cues.forEach {
            when (it) {
                is CharacterCue -> {
                    addCharacterCue(it, scriptId, sceneId, cuePosition)
                    cuePosition += it.parts.size
                }
                is ActionCue -> {
                    addActionCue(it, sceneId, cuePosition)
                    cuePosition++
                }
                is LyricsCue -> {
                    addLyricsCue(it, sceneId, cuePosition)
                    cuePosition++
                }
            }

        }
    }

    private fun addCharacterCue(characterCue: CharacterCue,
                                scriptId: Long,
                                sceneId: Long,
                                cuePosition: Int) {
        val existingCharacterModel = appDatabase.characterDao().getByNameInScript(characterCue.characterName, scriptId)

        val characterId = existingCharacterModel?.characterId ?: appDatabase.characterDao().insertOrReplace(
                CharacterDbModel(
                        scriptId = scriptId,
                        name = characterCue.characterName
                )
        )

        var partPosition = 0
        characterCue.parts.forEach {
            addCharacterCuePart(it, characterCue.characterExtension, sceneId, characterId, cuePosition + partPosition)
            partPosition++
        }
    }

    private fun addActionCue(actionCue: ActionCue,
                             sceneId: Long,
                             cuePosition: Int) {
        val cueModel = CueDbModel(
                sceneId = sceneId,
                characterId = null,
                type = CueDbModel.TYPE_ACTION,
                characterExtension = null,
                position = cuePosition,
                content = actionCue.direction,
                isBookmarked = false
        )
        appDatabase.cueDao().insert(cueModel)
    }

    private fun addLyricsCue(lyricsCue: LyricsCue,
                             sceneId: Long,
                             cuePosition: Int) {
        val cueModel = CueDbModel(
                sceneId = sceneId,
                characterId = null,
                type = CueDbModel.TYPE_LYRICS,
                characterExtension = null,
                position = cuePosition,
                content = lyricsCue.lyrics,
                isBookmarked = false
        )
        appDatabase.cueDao().insert(cueModel)
    }

    private fun addCharacterCuePart(part: CharacterCue.Part,
                                    extension: String?,
                                    sceneId: Long,
                                    characterId: Long,
                                    position: Int) {
        val (type, content) = when (part) {
            is CharacterCue.Dialog -> CueDbModel.TYPE_DIALOG to part.line
            is CharacterCue.Parenthetical -> CueDbModel.TYPE_ACTION to part.direction
            is CharacterCue.Lyrics -> CueDbModel.TYPE_LYRICS to part.lyrics
            else -> CueDbModel.TYPE_UNKNOWN to ""
        }

        val cueModel = CueDbModel(
                sceneId = sceneId,
                characterId = characterId,
                type = type,
                characterExtension = extension,
                position = position,
                content = content,
                isBookmarked = false
        )
        appDatabase.cueDao().insert(cueModel)
    }

}
