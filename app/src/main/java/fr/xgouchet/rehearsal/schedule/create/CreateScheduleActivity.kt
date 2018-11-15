package fr.xgouchet.rehearsal.schedule.create

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.Item

class CreateScheduleActivity
    : ArchXActivity<CreateScheduleContract.Presenter, CreateScheduleContract.View, List<Item.ViewModel>>() {


    private var scriptId: Int = 0

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        scriptId = intent.getIntExtra(EXTRA_SCRIPT_ID, 0)

        if (scriptId <= 0) {
            finish()
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/$scriptId"

    override fun instantiateFragment(): CreateScheduleContract.View {
        return CreateScheduleFragment()
    }

    override fun instantiatePresenter(): CreateScheduleContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSink = CreateScheduleDataSink(applicationContext)

        return CreateSchedulePresenter(scriptId, lifecycleOwner, dataSink)
    }

    // endregion

    // region ArchXActivity / FAB

    override fun getFabIcon(): Int? = R.drawable.ic_add_range

    override fun onFabClicked() {
        presenter.onAddRangeClicked()
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "create_schedule"

        private const val EXTRA_SCRIPT_ID = "fr.xgouchet.rehearsal.extra.SCRIPT_ID"

        @JvmStatic
        fun createIntent(context: Context, scriptId: Int): Intent {
            val intent = Intent(context, CreateScheduleActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT_ID, scriptId)

            return intent
        }
    }
}
