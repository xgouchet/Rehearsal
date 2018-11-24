package fr.xgouchet.rehearsal.screen.rehearsal

import android.content.Context
import android.content.Intent
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.sink.RangeSink
import fr.xgouchet.rehearsal.core.sink.RehearsalSink
import fr.xgouchet.rehearsal.core.source.AllRangesInRehearsalSource
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item

class RehearsalActivity
    : ArchXActivity<RehearsalContract.Presenter, RehearsalContract.View, List<Item.ViewModel>>() {


    private lateinit var rehearsal: Rehearsal

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        val intentRehearsal = intent.getParcelableExtra<Rehearsal>(EXTRA_REHEARSAL)

        if (intentRehearsal == null) {
            finish()
        } else {
            rehearsal = intentRehearsal
            title = DateFormatter.formatDate(intentRehearsal.dueDate)
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/${rehearsal.rehearsalId}"

    override fun instantiateFragment(): RehearsalContract.View {
        return RehearsalFragment()
    }

    override fun instantiatePresenter(): RehearsalContract.Presenter {
        val dataSource = AllRangesInRehearsalSource(applicationContext, rehearsal.rehearsalId)
        val rehearsalSink = RehearsalSink(applicationContext)
        val rangeSink = RangeSink(applicationContext)
        val transformer = RehearsalViewModelTransformer()

        return RehearsalPresenter(rehearsal, dataSource, rehearsalSink, rangeSink, transformer, RuntimeSchedulerProvider)
    }

    // endregion

    // region ArchXActivity / FAB

    override fun getFabIcon(): Int? = R.drawable.ic_add_range

    override fun onFabClicked() {
        presenter.onAddRangeClicked()
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "schedule_details"

        private const val EXTRA_REHEARSAL = "fr.xgouchet.rehearsal.extra.REHEARSAL"

        @JvmStatic
        fun createIntent(context: Context, rehearsal: Rehearsal): Intent {
            val intent = Intent(context, RehearsalActivity::class.java)

            intent.putExtra(EXTRA_REHEARSAL, rehearsal)

            return intent
        }
    }
}
