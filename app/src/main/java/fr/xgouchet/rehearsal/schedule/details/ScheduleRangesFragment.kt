package fr.xgouchet.rehearsal.schedule.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.scene.SceneActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class ScheduleRangesFragment
    : ItemListFragment(),
        ScheduleRangesContract.View {

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
                confirmDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDelete() {
        val currentActivity = activity ?: return

        AlertDialog.Builder(currentActivity)
                .setTitle(R.string.schedule_title_confirmDelete)
                .setMessage(R.string.schedule_text_deleteWarningPrompt)
                .setNeutralButton(android.R.string.cancel) { a, _ -> a.dismiss() }
                .setNegativeButton(android.R.string.ok) { _, _ ->
                    (presenter as? ScheduleRangesContract.Presenter)?.onDeleteActionSelected()
                }
                .create()
                .show()
    }

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? ScheduleRangesContract.Presenter)?.onItemSelected(item)
        return true
    }

    // endregion

    // region ScheduleRangesContract.View

    override fun showError(throwable: Throwable) {
        Snackbar.make(contentView, throwable.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    override fun navigateToSceneWithRange(scene: SceneModel, range: Pair<Int, Int>) {
        val currentActivity = activity ?: return
        val intent = SceneActivity.createIntent(currentActivity, scene, range)
        startActivity(intent)
    }


    override fun navigateBack() {
        activity?.finish()
    }
    // endregion
}
