package fr.xgouchet.rehearsal.voice.service

import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.voice.tts.TTSEngine
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class VoiceSceneReader(
        private val dataSourceProvider: (Long) -> ArchXDataSource<List<Cue>>,
        private val listener: SceneReader.Listener,
        private val schedulerProvider: SchedulerProvider,
        private val ttsEngineProvider: (String?) -> TTSEngine
) : SceneReader,
        TTSEngine.Listener {

    private var listeningDisposable: Disposable? = null

    private var firstCueId: Long = -1L

    private var cueQueue: List<Cue> = emptyList()

    private lateinit var dataSource: ArchXDataSource<List<Cue>>

    private var index = -1
    private var isStopped = true
    private var currentCue: Cue? = null

    private val ttsEngines: MutableMap<Long, TTSEngine> = mutableMapOf()

    // region VoiceSceneReader

    override fun playSceneFromCue(sceneId: Long, cueId: Long) {
        if (!isStopped) {
            stopSpeakingEngine()
        }
        listeningDisposable?.dispose()

        index = -1
        currentCue = null
        isStopped = false
        firstCueId = cueId

        dataSource = dataSourceProvider(sceneId)
        listeningDisposable = dataSource.listenData()
                .schedule(schedulerProvider)
                .subscribe(
                        {
                            val shouldPlay = !isStopped
                            pauseScene()
                            ttsEngines.clear()
                            onCuesRetrieved(it, firstCueId, shouldPlay)
                        },
                        { Timber.e(it, "#error #listening to cues") }
                )
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

    private fun onCuesRetrieved(cues: List<Cue>, cueId: Long, play: Boolean) {
        cueQueue = cues

        isStopped = !play

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

    private suspend fun speakCue(cue: Cue) {
        when (cue.type) {
            CueDbModel.TYPE_ACTION -> {
                Timber.d("#voice ignoring action : ${cue.content}")
                readNextCue()
            }
            CueDbModel.TYPE_DIALOG,
            CueDbModel.TYPE_LYRICS -> {
                if (cue.character?.isHidden == true) {
                    silentDialog(cue)
                } else {
                    speakDialogCue(cue)
                }
            }
            else -> Timber.w("#voice unknown type ${cue.type}")
        }
    }

    private suspend fun silentDialog(cue: Cue) {
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

    private suspend fun speakDialogCue(cue: Cue) {
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

    private fun getEngine(characterId: Long, name: String?): TTSEngine {
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
