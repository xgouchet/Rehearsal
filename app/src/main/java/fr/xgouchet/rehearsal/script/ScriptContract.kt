package fr.xgouchet.rehearsal.script

import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

interface ScriptContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemSelected(item:  Item.ViewModel)

        fun onScheduleActionSelected()
        fun onCastActionSelected()
        fun onDeleteActionSelected()
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun navigateToScene(scene: SceneModel)
        fun navigateToCastSettings(scriptId: Int)
        fun navigateBack()
        fun navigateToSchedule(scriptId: Int, scriptTitle: String)
        fun showError(throwable: Throwable)
    }

    interface DataSource : ArchXDataSource<List<SceneModel>>

    interface DataSink : ArchXDataSink<List<SceneModel>>

    interface ScriptDataSink : ArchXDataSink<ScriptModel>

    interface Transformer : ArchXViewModelTransformer<List<SceneModel>, List<Item.ViewModel>>
}
