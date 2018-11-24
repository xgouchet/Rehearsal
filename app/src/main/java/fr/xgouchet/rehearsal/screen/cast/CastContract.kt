package fr.xgouchet.rehearsal.screen.cast

import androidx.annotation.ColorInt
import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.transformer.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.ui.Item

interface CastContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemValueChanged(item: Item.ViewModel, value: String?)
        fun onItemSelected(item: Item.ViewModel)
        fun onColorPicked(requestId: Long, @ColorInt color: Int)
        fun onEnginePicked(requestId: Long, engine: String)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showColorPicker(requestId: Long, @ColorInt color: Int)
        fun showEnginePicker(requestId: Long, engine: String?)

        fun showError(throwable: Throwable)
    }


    interface Transformer : ArchXViewModelTransformer<List<Character>, List<Item.ViewModel>>

}
