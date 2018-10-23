package fr.xgouchet.rehearsal.scene

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.ui.Item

class SceneActivity
    : ArchXActivity<SceneContract.Presenter, SceneContract.View, List<Item.ViewModel>>() {


    private var sceneId: Int = 0
    private var sceneTitle: String = ""

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        sceneId = intent.getIntExtra(EXTRA_SCENE_ID, 0)
        sceneTitle = intent.getStringExtra(EXTRA_SCENE_TITLE).orEmpty()

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
        val dataSource = SceneDataSource(applicationContext, sceneId)
        val transformer = SceneViewModelTransformer()

        return ScenePresenter(lifecycleOwner, dataSource, transformer)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "scene"

        private const val EXTRA_SCENE_ID = "fr.xgouchet.rehearsal.extra.SCENE_ID"
        private const val EXTRA_SCENE_TITLE = "fr.xgouchet.rehearsal.extra.SCENE_TITLE"

        @JvmStatic
        fun createIntent(context: Context, sceneModel: SceneModel): Intent {
            val intent = Intent(context, SceneActivity::class.java)

            intent.putExtra(EXTRA_SCENE_ID, sceneModel.id)
            intent.putExtra(EXTRA_SCENE_TITLE, sceneModel.description)

            return intent
        }
    }
}
