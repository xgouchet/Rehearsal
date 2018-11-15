package fr.xgouchet.rehearsal.schedule.list

import fr.xgouchet.rehearsal.schedule.create.CreateScheduleActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment


class ScriptScheduleFragment
    : ItemListFragment(),
        ScriptScheduleContract.View {

    // region Fragment

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? ScriptScheduleContract.Presenter)?.onItemSelected(item)
        return true
    }

    // endregion

    // region ScriptScheduleContract.View

    override fun navigateToScheduleCreation(scriptId: Int, scriptTitle: String) {
        val currentActivity = activity ?: return
        val intent = CreateScheduleActivity.createIntent(currentActivity, scriptId )
        startActivity(intent)
    }

    // endregion
}
