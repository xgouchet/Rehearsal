package fr.xgouchet.rehearsal.screen.rehearsal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.screen.range.RangePickerActivity
import fr.xgouchet.rehearsal.screen.scene.SceneActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class RehearsalFragment
    : ItemListFragment(),
        RehearsalContract.View {

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.schedule, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_delete -> {
                confirmDelete(R.string.schedule_title_confirmDelete, R.string.schedule_text_deleteWarningPrompt) {
                    (presenter as? RehearsalContract.Presenter)?.onDeleteActionSelected()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_RANGE_PICKER -> {
                    val range = data?.getParcelableExtra<Range>(RangePickerActivity.EXTRA_RANGE)
                    if (range != null) {
                        (presenter as? RehearsalContract.Presenter)?.onRangeCreated(range)
                    }
                }
            }
        }
    }

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? RehearsalContract.Presenter)?.onItemSelected(item)
        return true
    }

    // endregion

    // region ScheduleRangesContract.View

    override fun showError(throwable: Throwable) {
        Snackbar.make(contentView, throwable.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    override fun showRangePicker(rehearsal: Rehearsal) {
        val currentActivity = activity ?: return

        val intent = RangePickerActivity.createIntent(currentActivity, rehearsal)
        startActivityForResult(intent, REQUEST_RANGE_PICKER)
    }

    override fun navigateToSceneWithRange(scene: Scene, range: Pair<Int, Int>) {
        val currentActivity = activity ?: return
        val intent = SceneActivity.createIntent(currentActivity, scene, range)
        startActivity(intent)
    }

    override fun navigateBack() {
        activity?.finish()
    }

    // endregion

    companion object {
        private const val REQUEST_RANGE_PICKER = 12
    }
}
