package fr.xgouchet.rehearsal.schedule.details

import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.scene.SceneActivity
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class ScheduleRangesFragment
    : ItemListFragment(),
        ScheduleRangesContract.View {


    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        (presenter as? ScheduleRangesContract.Presenter)?.onItemSelected(item)
        return true
    }

    // endregion

    // region ScheduleRangesContract.View

    override fun navigateToSceneWithRange(scene: SceneModel,  range: Pair<Int, Int>) {
        val currentActivity = activity ?: return
        val intent = SceneActivity.createIntent(currentActivity, scene, range)
        startActivity(intent)
    }

    // endregion
}
