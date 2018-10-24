package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.script.ScriptActivity
import fr.xgouchet.rehearsal.ui.ItemListFragment

class HomeFragment
    : ItemListFragment(),
        HomeContract.View {

    // region ItemListFragment

    override fun onItemAction(item: Any, action: String, value: String?) {
        (presenter as? HomeContract.Presenter)?.onItemSelected(item)
    }

    // endregion

    // region HomeContract.View

    override fun navigateToScript(script: ScriptModel) {
        val currentActivity = activity ?: return
        val intent = ScriptActivity.createIntent(currentActivity, script)
        startActivity(intent)
    }

    //endregion
}
