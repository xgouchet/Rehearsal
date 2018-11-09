package fr.xgouchet.rehearsal.voice.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.voice.tts.TTSEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class VoiceSceneReader(
        context: Context,
        private val owner: LifecycleOwner,
        private val listener: SceneReader.Listener,
        private val ttsEngineProvider: (String?) -> TTSEngine
) : SceneReader,
        TTSEngine.Listener {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context.applicationContext)
    private var observedData: LiveData<List<CueWithCharacter>>? = null
    private val observer = Observer<List<CueWithCharacter>> { onCuesRetrieved(it, firstCueId) }
    private var firstCueId = -1

    private var cueQueue: List<CueWithCharacter> = emptyList()

    private var index = -1
    private var isStopped = true
    private var currentCue: CueWithCharacter? = null

    private val ttsEngines: MutableMap<Int, TTSEngine> = mutableMapOf()

    // region VoiceSceneReader

    override fun playSceneFromCue(sceneId: Int, cueId: Int) {
        if (!isStopped) {
            stopSpeakingEngine()
        }
        observedData?.removeObserver(observer)

        index = -1
        currentCue = null
        isStopped = false
        firstCueId = cueId
        observedData = appDatabase.cueDao()
                .getAllInScene(sceneId)

        observedData?.observe(owner, observer)
    }

    override fun pauseScene() {
        isStopped = true
        stopSpeakingEngine()
        listener.stopped()
    }

    override fun resume() {
        if (!isStopped) return
        isStopped = false
        readCurrentCue()
    }

    // endregion

    // region TTSEngine.Listener

    override fun onStart(utteranceId: String) {

    }

    override fun onDone(utteranceId: String) {
        val utteranceIdPrefix = "${currentCue?.cueId}/"
        if (utteranceId.startsWith(utteranceIdPrefix)) {
            readNextCue()
        }
    }

    // endregion

    // region Internal

    private fun onCuesRetrieved(cues: List<CueWithCharacter>, cueId: Int) {
        cueQueue = cues

        if (index < 0 || index >= cues.size) {
            val foundIndex = cues.indexOfFirst { it.cueId == cueId }
            if (foundIndex >= 0) {
                Timber.d("#voice found cue with @id:$cueId at @index:$foundIndex : @cue:${cues[foundIndex]}")
                index = foundIndex
                readCurrentCue()
            } else {
                listener.stopped()
                Timber.w("#voice couldn't find cue with @id:$cueId in ${cues.size}")
            }
        } else {
            readCurrentCue()
        }

    }

    private fun readNextCue() {
        index++
        readCurrentCue()
    }

    private fun readCurrentCue() {
        if (isStopped) {
            listener.stopped()
            return
        }

        val cue = cueQueue.getOrNull(index)
        currentCue = cue

        if (cue == null) {
            Timber.w("#voice no cue at @index:$index")
            listener.stopped()
        } else {
            listener.readingCue(cue)
            GlobalScope.launch {
                speakCue(cue)
            }
        }

    }

    private suspend fun speakCue(cue: CueWithCharacter) {
        when (cue.type) {
            CueModel.TYPE_ACTION -> {
                Timber.d("#voice ignoring action : ${cue.content}")
                readNextCue()
            }
            CueModel.TYPE_DIALOG,
            CueModel.TYPE_LYRICS -> {
                if (cue.character?.isHidden == true) {
                    silentDialog(cue)
                } else {
                    speakDialogCue(cue)
                }
            }
            else -> Timber.w("#voice unknown type ${cue.type}")
        }
    }

    private suspend fun silentDialog(cue: CueWithCharacter) {
        val length = cue.content.length.toFloat()

        val factor = (SILENT_FACTOR_A / length) + SILENT_FACTOR_C
        val duration = (length * factor).toLong()

        withContext(Dispatchers.IO) {
            Timber.d("#voice #sleeping for @duration:$duration ms with cue @length:$length")
            delay(duration)
        }

        if (currentCue == cue) {
            readNextCue()
        }
    }

    private suspend fun speakDialogCue(cue: CueWithCharacter) {
        Timber.i("#voice reading cue at @index:$index")
        val character = cue.character ?: return

        val characterId = character.characterId
        val engine = getEngine(characterId, character.ttsEngine)

        while (!engine.isReady()) {
            withContext(Dispatchers.IO) {
                delay(WAIT_FOR_ENGINE_STEP_MS)
            }
        }

        val utteranceId = "${cue.cueId}/${System.currentTimeMillis()}"
        engine.setPitch(character.ttsPitch)
        engine.setRate(character.ttsRate)
        // TODO setLocale ?
        engine.speak(
                message = cue.content,
                utteranceId = utteranceId)
    }

    private fun stopSpeakingEngine() {
        val speakingEngine = ttsEngines[currentCue?.character?.characterId]
        speakingEngine?.stop()
    }

    private fun getEngine(characterId: Int, name: String?): TTSEngine {
        return ttsEngines.getOrPut(characterId) {
            ttsEngineProvider(name).apply {
                setListener(this@VoiceSceneReader)
                Thread.sleep(500)
            }
        }
    }

    // endregion

    companion object {

        const val SILENT_FACTOR_C = 40
        const val SILENT_FACTOR_A = 550

        const val WAIT_FOR_ENGINE_STEP_MS = 100L
    }
}
