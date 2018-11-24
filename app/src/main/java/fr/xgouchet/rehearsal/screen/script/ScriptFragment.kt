package fr.xgouchet.rehearsal.screen.script

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.screen.cast.CastActivity
import fr.xgouchet.rehearsal.screen.scene.SceneActivity
import fr.xgouchet.rehearsal.screen.schedule.ScheduleActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class ScriptFragment
    : ItemListFragment(),
        ScriptContract.View {


    private var showEmptyScenes = true

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.script, menu)

        menu.findItem(R.id.action_show_empty_scenes).isVisible = !showEmptyScenes
        menu.findItem(R.id.action_hide_empty_scenes).isVisible = showEmptyScenes
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var handled = true
        when (item?.itemId) {
            R.id.action_schedule -> (presenter as? ScriptContract.Presenter)?.onScheduleActionSelected()
            R.id.action_cast -> (presenter as? ScriptContract.Presenter)?.onCastActionSelected()
            R.id.action_delete -> confirmDelete()
            R.id.action_hide_empty_scenes -> (presenter as? ScriptContract.Presenter)?.onHideEmptyScenes()
            R.id.action_show_empty_scenes -> (presenter as? ScriptContract.Presenter)?.onShowEmptyScenes()
            else -> handled = super.onOptionsItemSelected(item)
        }
        return handled
    }

    private fun confirmDelete() {
        val currentActivity = activity ?: return

        AlertDialog.Builder(currentActivity)
                .setTitle(R.string.script_title_confirmDelete)
                .setMessage(R.string.script_text_deleteWarningPrompt)
                .setNeutralButton(android.R.string.cancel) { a, _ -> a.dismiss() }
                .setNegativeButton(android.R.string.ok) { _, _ ->
                    (presenter as? ScriptContract.Presenter)?.onDeleteActionSelected()
                }
                .create()
                .show()
    }

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? ScriptContract.Presenter)?.onItemSelected(item)
        return true
    }

    // endregion

    // region ScriptContract.View

    override fun navigateToScene(scene: Scene) {
        val currentActivity = activity ?: return
        val intent = SceneActivity.createIntent(currentActivity, scene)
        startActivity(intent)
    }

    override fun navigateToSchedule(script: Script) {
        val currentActivity = activity ?: return
        val intent = ScheduleActivity.createIntent(currentActivity, script)
        startActivity(intent)
    }

    override fun navigateToCastSettings(script: Script) {
        val currentActivity = activity ?: return
        val intent = CastActivity.createIntent(currentActivity, script)
        startActivity(intent)
    }

    override fun showError(throwable: Throwable) {
        showSnackbarError(throwable)
    }


    override fun navigateBack() {
        activity?.finish()
    }

    override fun setEmptyScenesVisible(showEmptyScenes: Boolean) {
        this.showEmptyScenes = showEmptyScenes
        activity?.invalidateOptionsMenu()
    }
    // endregion
}
