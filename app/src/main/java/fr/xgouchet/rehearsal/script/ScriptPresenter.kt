package fr.xgouchet.rehearsal.script

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.ui.Item

class ScriptPresenter(owner: LifecycleOwner,
                      dataSource: ScriptContract.DataSource,
                      transformer: ScriptContract.Transformer)
    : ArchXDataPresenter<List<SceneModel>, ScriptContract.View, List<Item.ViewModel>>(owner, dataSource, transformer),
        ScriptContract.Presenter {

    override fun onItemSelected(item: Any) {
        val scene = (item as? Item.ViewModel)?.data as? SceneModel

        if (scene != null) {
            view?.navigateToScene(scene)
        }
    }
}
