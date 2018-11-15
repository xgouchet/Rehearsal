package fr.xgouchet.rehearsal.schedule.list

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.MarkdownConverter

class ScriptScheduleActivity
    : ArchXActivity<ScriptScheduleContract.Presenter, ScriptScheduleContract.View, List<Item.ViewModel>>() {


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

    override fun instantiateFragment(): ScriptScheduleContract.View {
        return ScriptScheduleFragment()
    }

    override fun instantiatePresenter(): ScriptScheduleContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSource = ScriptScheduleDataSource(applicationContext, scriptId)
        val dataSink = ScriptScheduleDataSink(applicationContext)
        val transformer = ScriptScheduleViewModelTransformer()

        return ScriptSchedulePresenter(scriptId, scriptTitle, lifecycleOwner, dataSource, dataSink, transformer)
    }

    // endregion

    // region ArchXActivity / FAB

    override fun getFabIcon(): Int? = R.drawable.ic_add

    override fun onFabClicked() {
        presenter.onAddSchedule()
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "script_schedule"

        private const val EXTRA_SCRIPT_ID = "fr.xgouchet.rehearsal.extra.SCRIPT_ID"
        private const val EXTRA_SCRIPT_TITLE = "fr.xgouchet.rehearsal.extra.SCRIPT_TITLE"

        @JvmStatic
        fun createIntent(context: Context, scriptId: Int, scriptTitle: String): Intent {
            val intent = Intent(context, ScriptScheduleActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT_ID, scriptId)
            intent.putExtra(EXTRA_SCRIPT_TITLE, scriptTitle)

            return intent
        }
    }
}
