package fr.xgouchet.rehearsal.screen.scene

import android.content.Context
import android.content.Intent
import android.os.Bundle
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.sink.CuesSink
import fr.xgouchet.rehearsal.core.sink.PropSink
import fr.xgouchet.rehearsal.core.source.AllCharactersInSceneSource
import fr.xgouchet.rehearsal.core.source.AllCuesInSceneSource
import fr.xgouchet.rehearsal.core.source.AllPropsInScriptSource
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.voice.app.VoiceController

class SceneActivity
    : ArchXActivity<SceneContract.Presenter, SceneContract.View, List<Item.ViewModel>>() {


    private lateinit var scene: Scene

    private var range: Pair<Int, Int>? = null

    private lateinit var voiceController: VoiceController

    // region Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        voiceController = VoiceController(this)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        voiceController.connectListener()
    }

    override fun onStop() {
        super.onStop()
        voiceController.disconnectListener()
    }

    // endregion

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        val intentScene = intent.getParcelableExtra<Scene>(EXTRA_SCENE)

        if (intentScene == null) {
            finish()
        } else {

            val startCueId = intent.getIntExtra(EXTRA_RANGE_START_POSITION, -1)
            val endCueId = intent.getIntExtra(EXTRA_RANGE_END_POSITION, -1)

            if (startCueId >= 0 && endCueId >= 0) {
                range = startCueId to endCueId
            }

            title = intentScene.title
            scene = intentScene
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/${scene.sceneId}"

    override fun instantiateFragment(): SceneContract.View {
        return SceneFragment()
    }

    override fun instantiatePresenter(): SceneContract.Presenter {
        val dataSource = AllCuesInSceneSource(applicationContext, scene.sceneId)
        val characterSource = AllCharactersInSceneSource(applicationContext, scene.sceneId)
        val propsSource = AllPropsInScriptSource(applicationContext, scene.scriptId)
        val dataSink = CuesSink(applicationContext)
        val propSink = PropSink(applicationContext)
        val transformer = SceneViewModelTransformer()

        return ScenePresenter(
                scene,
                range,
                voiceController,
                dataSource,
                characterSource,
                propsSource,
                dataSink,
                propSink,
                transformer,
                RuntimeSchedulerProvider
        )
    }

    // endregion

    // region ArchXActivity / FAB

    override fun getFabIcon(): Int? {
        return R.drawable.ic_play
    }

    override fun onFabClicked() {
        presenter.onPlayPauseSelected()
    }

    fun showReading(reading: Boolean) {
        fab.setImageResource(if (reading) R.drawable.ic_pause else R.drawable.ic_play)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "scene"

        private const val EXTRA_SCENE = "fr.xgouchet.rehearsal.extra.SCENE"

        private const val EXTRA_RANGE_START_POSITION = "fr.xgouchet.rehearsal.extra.RANGE_START_POSITION"
        private const val EXTRA_RANGE_END_POSITION = "fr.xgouchet.rehearsal.extra.RANGE_END_POSITION"

        @JvmStatic
        fun createIntent(context: Context, scene: Scene): Intent {
            val intent = Intent(context, SceneActivity::class.java)

            intent.putExtra(EXTRA_SCENE, scene)

            return intent
        }

        @JvmStatic
        fun createIntent(context: Context, scene: Scene, range: Pair<Int, Int>): Intent {
            val intent = Intent(context, SceneActivity::class.java)

            intent.putExtra(EXTRA_SCENE, scene)
            intent.putExtra(EXTRA_RANGE_START_POSITION, range.first)
            intent.putExtra(EXTRA_RANGE_END_POSITION, range.second)

            return intent
        }
    }
}
