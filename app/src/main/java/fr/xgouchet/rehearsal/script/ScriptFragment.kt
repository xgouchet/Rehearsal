package fr.xgouchet.rehearsal.script

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.cast.CastActivity
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.scene.SceneActivity
import fr.xgouchet.rehearsal.ui.ItemListFragment

class ScriptFragment
    : ItemListFragment(),
        ScriptContract.View {

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.script, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_cast -> {
                (presenter as? ScriptContract.Presenter)?.onCastActionSelected()
                true
            }
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

    override fun onItemAction(item: Any, action: String, value: String?) {
        (presenter as? ScriptContract.Presenter)?.onItemSelected(item)
    }

    // endregion

    // region ScriptContract.View

    override fun navigateToScene(scene: SceneModel) {
        val currentActivity = activity ?: return
        val intent = SceneActivity.createIntent(currentActivity, scene)
        startActivity(intent)
    }

    override fun navigateToCastSettings(scriptId: Int) {
        val currentActivity = activity ?: return
        val intent = CastActivity.createIntent(currentActivity, scriptId)
        startActivity(intent)
    }

    override fun navigateBack() {
        activity?.finish()
    }

    // endregion
}
