package fr.xgouchet.rehearsal.script

import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.scene.SceneActivity
import fr.xgouchet.rehearsal.ui.ItemListFragment

class ScriptFragment
    : ItemListFragment(),
        ScriptContract.View {

    override fun onItemSelected(item: Any) {
        (presenter as? ScriptContract.Presenter)?.onItemSelected(item)
    }

    override fun navigateToScene(scene: SceneModel) {
        val currentActivity = activity ?: return
        val intent = SceneActivity.createIntent(currentActivity, scene)
        startActivity(intent)
    }
}
