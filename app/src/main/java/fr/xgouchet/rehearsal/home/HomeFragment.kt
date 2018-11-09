package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.script.ScriptActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class HomeFragment
    : ItemListFragment(),
        HomeContract.View {

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? HomeContract.Presenter)?.onItemSelected(item)
        return true
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
