package fr.xgouchet.rehearsal.cast

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.ui.Item

class CastPresenter(
        private val dataSink: CastContract.DataSink,
        owner: LifecycleOwner,
        dataSource: CastContract.DataSource,
        transformer: CastContract.Transformer
) : ArchXDataPresenter<List<CharacterModel>, CastContract.View, List<Item.ViewModel>>(owner, dataSource, transformer),
        CastContract.Presenter {


    private val mutableDataSource: CastContract.DataSource = dataSource

    // region CastContract.Presenter

    override fun onItemValueChanged(item: Any, value: String?) {

        val character = (item as? Item.ViewModel)?.data as? CharacterModel ?: return

        val updatedCharacter = character.copy(isSelected = value?.toBoolean() ?: false)

        dataSink.update(updatedCharacter)
    }

    // endregion
}
