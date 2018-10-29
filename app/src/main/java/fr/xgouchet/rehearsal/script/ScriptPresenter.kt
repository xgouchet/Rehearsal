package fr.xgouchet.rehearsal.script

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

class ScriptPresenter(
        private val scriptId: Int,
        owner: LifecycleOwner,
        dataSource: ScriptContract.DataSource,
        dataSink: ScriptContract.DataSink,
        private val scriptDataSink: ScriptContract.ScriptDataSink,
        transformer: ScriptContract.Transformer
) : ArchXDataPresenter<List<SceneModel>, ScriptContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        ScriptContract.Presenter {


    // region ScriptContract.Presenter

    override fun onItemSelected(item: Any) {
        val scene = (item as? Item.ViewModel)?.data as? SceneModel

        if (scene != null) {
            view?.navigateToScene(scene)
        }
    }

    override fun onCastActionSelected() {
        view?.navigateToCastSettings(scriptId)
    }

    override fun onDeleteActionSelected() {
        scriptDataSink.deleteData(ScriptModel(scriptId = scriptId, title = "", author = ""))
        view?.navigateBack()
    }

    // endregion

}
