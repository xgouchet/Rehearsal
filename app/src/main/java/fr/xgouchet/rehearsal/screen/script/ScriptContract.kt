package fr.xgouchet.rehearsal.screen.script

import android.net.Uri
import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Scene
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.ui.Item

interface ScriptContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {

        fun onItemSelected(item: Item.ViewModel)

        fun onScheduleActionSelected()
        fun onCastActionSelected()
        fun onDeleteActionSelected()
        fun onHideEmptyScenes()
        fun onShowEmptyScenes()
        fun onExportScript()
        fun onExportScriptToUri(uri: Uri)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun navigateToScene(scene: Scene)
        fun navigateToCastSettings(script: Script)
        fun navigateBack()
        fun navigateToSchedule(script: Script)
        fun showError(throwable: Throwable)
        fun setEmptyScenesVisible(showEmptyScenes: Boolean)
        fun requestDocumentUri(filename: String)
    }

    interface Transformer : ArchXViewModelTransformer<List<Scene>, List<Item.ViewModel>>
}
