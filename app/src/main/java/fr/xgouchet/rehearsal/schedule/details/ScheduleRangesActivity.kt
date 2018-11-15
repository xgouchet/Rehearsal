package fr.xgouchet.rehearsal.schedule.details

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.MarkdownConverter

class ScheduleRangesActivity
    : ArchXActivity<ScheduleRangesContract.Presenter, ScheduleRangesContract.View, List<Item.ViewModel>>() {


    private var scheduleId: Int = 0
    private var scheduleTitle: String = ""

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        scheduleId = intent.getIntExtra(EXTRA_SCHEDULE_ID, 0)
        scheduleTitle = intent.getStringExtra(EXTRA_SCHEDULE_TITLE).orEmpty()

        if (scheduleId <= 0) {
            finish()
        }

        title = MarkdownConverter.parse(scheduleTitle)
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/$scheduleId"

    override fun instantiateFragment(): ScheduleRangesContract.View {
        return ScheduleRangesFragment()
    }

    override fun instantiatePresenter(): ScheduleRangesContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSource = ScheduleRangesDataSource(applicationContext, scheduleId)
        val dataSink = ScheduleDataSink(applicationContext)
        val transformer = ScheduleRangesViewModelTransformer()

        return ScheduleRangesPresenter(scheduleId, lifecycleOwner, dataSource, dataSink, transformer)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "schedule_details"

        private const val EXTRA_SCHEDULE_ID = "fr.xgouchet.rehearsal.extra.SCHEDULE_ID"
        private const val EXTRA_SCHEDULE_TITLE = "fr.xgouchet.rehearsal.extra.SCHEDULE_TITLE"

        @JvmStatic
        fun createIntent(context: Context, schedule: ScheduleModel): Intent {
            val intent = Intent(context, ScheduleRangesActivity::class.java)

            intent.putExtra(EXTRA_SCHEDULE_ID, schedule.scheduleId)
            intent.putExtra(EXTRA_SCHEDULE_TITLE, DateFormatter.formatDate(schedule.dueDate))

            return intent
        }
    }
}
