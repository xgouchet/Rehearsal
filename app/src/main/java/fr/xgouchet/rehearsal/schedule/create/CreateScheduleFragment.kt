package fr.xgouchet.rehearsal.schedule.create

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.range.RangePickerActivity
import fr.xgouchet.rehearsal.range.RangePickerContract
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_VALUE_CHANGED
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment
import java.util.Calendar
import java.util.Date


class CreateScheduleFragment
    : ItemListFragment(),
        CreateScheduleContract.View {


    private var isValid: Boolean = false

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.form, menu)

        menu.findItem(R.id.action_validate).isVisible = isValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_validate) {
            (presenter as? CreateScheduleContract.Presenter)?.onValidate()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_RANGE_PICKER -> onRangePicked(resultCode, data)
        }
    }

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        var consumed = true
        when (action) {
            ACTION_DEFAULT -> (presenter as? CreateScheduleContract.Presenter)?.onItemSelected(item)
            ACTION_VALUE_CHANGED -> (presenter as? CreateScheduleContract.Presenter)?.onItemValueChanged(item, value)
            else -> consumed = false
        }
        return consumed
    }

    // endregion

    // region CreateScheduleContract.View

    override fun showValidate(valid: Boolean) {
        this.isValid = valid
        activity?.invalidateOptionsMenu()
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
                    (presenter as? CreateScheduleContract.Presenter)?.onDatePicked(result.time)
                },
                year,
                month,
                dayOfMonth)

        dialog.show()
    }

    override fun showRangePicker(scriptId: Int) {
        val currentActivity = activity ?: return

        val intent = RangePickerActivity.createIntent(currentActivity, scriptId)
        startActivityForResult(intent, REQUEST_RANGE_PICKER)
    }


    override fun showError(throwable: Throwable) {
        Snackbar.make(contentView, throwable.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    override fun navigateBack() {
        activity?.finish()
    }

    // endregion

    // region Internal

    private fun onRangePicked(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val range = data?.getParcelableExtra<RangeModel>(RangePickerActivity.EXTRA_RANGE)
            if (range != null) {
                (presenter as? CreateScheduleContract.Presenter)?.onRangeCreated(range)
            }
        }
    }

    // endregion

    companion object {
        private const val REQUEST_RANGE_PICKER = 12
    }
}
