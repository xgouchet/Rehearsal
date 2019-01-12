package fr.xgouchet.rehearsal.screen.script

import android.content.Context
import android.net.Uri
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.core.room.model.SceneDbModel
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.io.FileOutputStream
import java.io.OutputStreamWriter


class ExportFountainDocument(
        private val context: Context,
        private val script: Script,
        private val uri: Uri
) : SingleOnSubscribe<Boolean> {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    override fun subscribe(emitter: SingleEmitter<Boolean>) {

        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "w")
        if (parcelFileDescriptor == null) {
            emitter.onError(NullPointerException("Cannot open destination file descriptor"))
            return
        }

        val fileOutputStream = FileOutputStream(parcelFileDescriptor.fileDescriptor)

        try {
            fileOutputStream.writer(Charsets.UTF_8)
                    .use {
                        writeScript(it)
                    }

            parcelFileDescriptor.close()

            emitter.onSuccess(true)
        } catch (e: Exception) {
            emitter.onError(e)
        }

    }

    private fun writeScript(writer: OutputStreamWriter) {
        writeHeader(writer)

        val scenes = appDatabase.sceneDao().getAllFromScript(script.scriptId).blockingFirst()
        scenes.forEach {
            writeScene(it, writer)
        }

    }

    private fun writeHeader(writer: OutputStreamWriter) {
        writer.write("Title: ")
        writer.write(script.title)
        writer.write("\n")

        writer.write("Authors: ")
        writer.write(script.author)
        writer.write("\n")

        writer.write("\n")
    }

    private fun writeScene(scene: SceneDbModel, writer: OutputStreamWriter) {
        writeSceneHeader(scene, writer)

        val cues = appDatabase.cueDao().getAllInScene(scene.sceneId).blockingFirst()
        val characters = appDatabase.characterDao().getAllFromScene(scene.sceneId).blockingFirst()
        var previousCharacter: CharacterDbModel? = null

        cues.forEach { cue ->
            val currentCharacter = characters.firstOrNull { it.characterId == cue.characterId }

            if (currentCharacter != previousCharacter) {
                writer.write("\n")
                if (currentCharacter != null) {
                    writeCharacterHeader(currentCharacter.name, cue.characterExtension, writer)
                }
            } else if (currentCharacter == null) {
                writer.write("\n")
            }

            writeCue(cue, writer)
            previousCharacter = currentCharacter
        }
        writer.write("\n")
    }

    private fun writeSceneHeader(scene: SceneDbModel, writer: OutputStreamWriter) {
        writer.write(".")
        writer.write(scene.description)
        if (scene.numbering.isNotBlank()) {
            writer.write(" #")
            writer.write(scene.numbering)
            writer.write("#")
        }

        writer.write("\n")
        writer.write("\n")
    }

    private fun writeCharacterHeader(characterName: String,
                                     characterExtension: String?,
                                     writer: OutputStreamWriter) {
        if (!characterName.matches(Regex("[A-Z0-9_\\- ]+"))) {
            writer.write(".")
        }
        writer.write(characterName)

        if (!characterExtension.isNullOrBlank()) {
            writer.write(" (")
            writer.write(characterExtension)
            writer.write(")")
        }
        writer.write("\n")
    }

    private fun writeCue(cue: CueDbModel, writer: OutputStreamWriter) {
        when (cue.type) {
            CueDbModel.TYPE_DIALOG -> writer.write(cue.content)
            CueDbModel.TYPE_ACTION -> {
                if (cue.characterId == null) {
                    writer.write(cue.content)
                } else {
                    writer.write("(")
                    writer.write(cue.content)
                    writer.write(")")
                }
            }
            CueDbModel.TYPE_LYRICS -> {
                writer.write("~")
                writer.write(cue.content)
            }
        }
        writer.write("\n")
    }


}
