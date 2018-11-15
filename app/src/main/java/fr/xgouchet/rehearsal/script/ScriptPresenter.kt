package fr.xgouchet.rehearsal.script

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.join.SceneWithCount
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

class ScriptPresenter(
        private val scriptId: Int,
        private val scriptTitle: String,
        owner: LifecycleOwner,
        dataSource: ScriptContract.DataSource,
        dataSink: ScriptContract.DataSink,
        private val scriptDataSink: ScriptContract.ScriptDataSink,
        transformer: ScriptContract.Transformer
) : ArchXDataPresenter<List<SceneWithCount>, ScriptContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        ScriptContract.Presenter {


    // region ScriptContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val scene = item.getItemData() as? SceneWithCount

        if (scene != null) {
            view?.navigateToScene(scene.asSceneModel())
        }
    }

    override fun onScheduleActionSelected() {
        view?.navigateToSchedule(scriptId, scriptTitle)
    }

    override fun onCastActionSelected() {
        view?.navigateToCastSettings(scriptId)
    }

    override fun onDeleteActionSelected() {
        scriptDataSink.deleteData(ScriptModel(scriptId = scriptId, title = "", author = "")) {
            if (it != null) {
                view?.showError(it)
            }
        }
        view?.navigateBack()
    }

    // endregion

}
