package fr.xgouchet.rehearsal.scene

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.voice.app.VoiceController

class SceneActivity
    : ArchXActivity<SceneContract.Presenter, SceneContract.View, List<Item.ViewModel>>() {


    private var sceneId: Int = 0
    private var sceneTitle: String = ""

    private var range: Pair<Int, Int>? = null

    private var linesVisible: Boolean = false

    private lateinit var voiceObserver: VoiceController

    // region Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        voiceObserver = VoiceController(this)
        super.onCreate(savedInstanceState)
    }

    // endregion

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        sceneId = intent.getIntExtra(EXTRA_SCENE_ID, 0)
        sceneTitle = intent.getStringExtra(EXTRA_SCENE_TITLE).orEmpty()

        val startCueId = intent.getIntExtra(EXTRA_RANGE_START_POSITION, 0)
        val endCueId = intent.getIntExtra(EXTRA_RANGE_END_POSITION, 0)

        if (startCueId > 0 && endCueId > 0) {
            range = startCueId to endCueId
        }

        if (sceneId <= 0) {
            finish()
        }

        title = sceneTitle
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/$sceneId"

    override fun instantiateFragment(): SceneContract.View {
        return SceneFragment()
    }

    override fun instantiatePresenter(): SceneContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSource = SceneDataSource(applicationContext, sceneId, range)
        val characterDataSource = CharactersDataSource(applicationContext, sceneId)
        val dataSink = SceneDataSink(applicationContext)
        val transformer = SceneViewModelTransformer()

        return ScenePresenter(sceneId, voiceObserver, lifecycleOwner, dataSource, dataSink, characterDataSource, transformer)
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

        private const val EXTRA_SCENE_ID = "fr.xgouchet.rehearsal.extra.SCENE_ID"
        private const val EXTRA_SCENE_TITLE = "fr.xgouchet.rehearsal.extra.SCENE_TITLE"
        private const val EXTRA_RANGE_START_POSITION = "fr.xgouchet.rehearsal.extra.RANGE_START_POSITION"
        private const val EXTRA_RANGE_END_POSITION = "fr.xgouchet.rehearsal.extra.RANGE_END_POSITION"

        @JvmStatic
        fun createIntent(context: Context, sceneModel: SceneModel): Intent {
            val intent = Intent(context, SceneActivity::class.java)

            intent.putExtra(EXTRA_SCENE_ID, sceneModel.sceneId)
            intent.putExtra(EXTRA_SCENE_TITLE, sceneModel.description)

            return intent
        }

        @JvmStatic
        fun createIntent(context: Context, sceneModel: SceneModel, range: Pair<Int, Int>): Intent {
            val intent = Intent(context, SceneActivity::class.java)

            intent.putExtra(EXTRA_SCENE_ID, sceneModel.sceneId)
            intent.putExtra(EXTRA_SCENE_TITLE, sceneModel.description)
            intent.putExtra(EXTRA_RANGE_START_POSITION, range.first)
            intent.putExtra(EXTRA_RANGE_END_POSITION, range.second)

            return intent
        }
    }
}
