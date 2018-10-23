package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.script.ScriptActivity
import fr.xgouchet.rehearsal.ui.ItemListFragment

class HomeFragment
    : ItemListFragment(),
        HomeContract.View {

    override fun onItemSelected(item: Any) {
        (presenter as? HomeContract.Presenter)?.onItemSelected(item)
    }

    override fun navigateToScript(script: ScriptModel) {
        val currentActivity = activity ?: return
        val intent = ScriptActivity.createIntent(currentActivity, script)
        startActivity(intent)
    }
}
