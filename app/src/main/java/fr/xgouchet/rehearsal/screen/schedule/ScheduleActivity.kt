package fr.xgouchet.rehearsal.screen.schedule

import android.content.Context
import android.content.Intent
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.sink.RehearsalSink
import fr.xgouchet.rehearsal.core.source.AllRehearsalsInScriptSource
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.MarkdownConverter

class ScheduleActivity
    : ArchXActivity<ScheduleContract.Presenter, ScheduleContract.View, List<Item.ViewModel>>() {


    private lateinit var script: Script

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        val intentScript = intent.getParcelableExtra<Script>(EXTRA_SCRIPT)

        if (intentScript == null) {
            finish()
        } else {
            script = intentScript
            title = MarkdownConverter.parse(intentScript.title)
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/${script.scriptId}"

    override fun instantiateFragment(): ScheduleContract.View {
        return ScheduleFragment()
    }

    override fun instantiatePresenter(): ScheduleContract.Presenter {
        val dataSource = AllRehearsalsInScriptSource(applicationContext, script.scriptId)
        val dataSink = RehearsalSink(applicationContext)
        val transformer = ScheduleViewModelTransformer()

        return SchedulePresenter(script, dataSource, dataSink, transformer, RuntimeSchedulerProvider)
    }

    // endregion

    // region ArchXActivity / FAB

    override fun getFabIcon(): Int? = R.drawable.ic_add_event

    override fun onFabClicked() {
        presenter.onAddSchedule()
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "script_schedule"

        private const val EXTRA_SCRIPT = "fr.xgouchet.rehearsal.extra.SCRIPT"

        @JvmStatic
        fun createIntent(context: Context, script: Script): Intent {
            val intent = Intent(context, ScheduleActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT, script)

            return intent
        }
    }
}
