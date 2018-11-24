package fr.xgouchet.rehearsal.screen.schedule

import android.app.DatePickerDialog
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.screen.rehearsal.RehearsalActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment
import java.util.Calendar
import java.util.Date


class ScheduleFragment
    : ItemListFragment(),
        ScheduleContract.View {

    // region Fragment

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? ScheduleContract.Presenter)?.onItemSelected(item)
        return true
    }

    // endregion

    // region ScheduleContract.View

    override fun navigateToSetDetails(rehearsal: Rehearsal) {
        val currentActivity = activity ?: return
        val intent = RehearsalActivity.createIntent(currentActivity, rehearsal)
        startActivity(intent)
    }


    override fun showDatePicker(dueDate: Date) {
        val currentActivity = activity ?: return

        val cal = Calendar.getInstance().apply { time = dueDate }
        val year: Int = cal[Calendar.YEAR]
        val month: Int = cal[Calendar.MONTH]
        val dayOfMonth: Int = cal[Calendar.DAY_OF_MONTH]

        val dialog = DatePickerDialog(currentActivity,
                { _, y, m, d ->
                    val result = Calendar.getInstance()
                    result[Calendar.YEAR] = y
                    result[Calendar.MONTH] = m
                    result[Calendar.DAY_OF_MONTH] = d
                    (presenter as? ScheduleContract.Presenter)?.onDatePicked(result.time)
                },
                year,
                month,
                dayOfMonth)

        dialog.show()
    }

    override fun showError(throwable: Throwable) {
        showSnackbarError(throwable)
    }
    // endregion
}
