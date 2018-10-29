package fr.xgouchet.rehearsal.script

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.MarkdownConverter

class ScriptActivity
    : ArchXActivity<ScriptContract.Presenter, ScriptContract.View, List<Item.ViewModel>>() {


    private var scriptId: Int = 0
    private var scriptTitle: String = ""

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        scriptId = intent.getIntExtra(EXTRA_SCRIPT_ID, 0)
        scriptTitle = intent.getStringExtra(EXTRA_SCRIPT_TITLE).orEmpty()

        if (scriptId <= 0) {
            finish()
        }

        title = MarkdownConverter.parse(scriptTitle)
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/$scriptId"

    override fun instantiateFragment(): ScriptContract.View {
        return ScriptFragment()
    }

    override fun instantiatePresenter(): ScriptContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSource = ScriptDataSource(applicationContext, scriptId)
        val dataSink = ScriptScenesDataSink()
        val scriptDataSink = ScriptDataSink(applicationContext)
        val transformer = ScriptViewModelTransformer()

        return ScriptPresenter(scriptId, lifecycleOwner, dataSource, dataSink, scriptDataSink, transformer)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "script"

        private const val EXTRA_SCRIPT_ID = "fr.xgouchet.rehearsal.extra.SCRIPT_ID"
        private const val EXTRA_SCRIPT_TITLE = "fr.xgouchet.rehearsal.extra.SCRIPT_TITLE"

        @JvmStatic
        fun createIntent(context: Context, scriptModel: ScriptModel): Intent {
            val intent = Intent(context, ScriptActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT_ID, scriptModel.scriptId)
            intent.putExtra(EXTRA_SCRIPT_TITLE, scriptModel.title)

            return intent
        }
    }
}
