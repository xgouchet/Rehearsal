package fr.xgouchet.rehearsal.cast

import androidx.annotation.ColorInt
import fr.xgouchet.archx.ArchXPresenter
import fr.xgouchet.archx.ArchXView
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.ui.Item

interface CastContract {

    interface Presenter : ArchXPresenter<List<Item.ViewModel>> {
        fun onItemValueChanged(item: Item.ViewModel, value: String?)
        fun onItemSelected(item: Item.ViewModel)
        fun onColorPicked(requestId: Int, @ColorInt color: Int)
        fun onEnginePicked(requestId: Int, engine: String)
    }

    interface View : ArchXView<List<Item.ViewModel>> {
        fun showColorPicker(requestId: Int, @ColorInt color: Int)
        fun showEnginePicker(requestId: Int, engine: String?)
    }

    interface DataSource : ArchXDataSource<List<CharacterModel>>

    interface DataSink : ArchXDataSink<List<CharacterModel>>

    interface Transformer : ArchXViewModelTransformer<List<CharacterModel>, List<Item.ViewModel>>

}
