package fr.xgouchet.rehearsal.cast

import androidx.annotation.ColorInt
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.ui.Item

class CastPresenter(
        owner: LifecycleOwner,
        dataSource: CastContract.DataSource,
        dataSink: CastContract.DataSink,
        transformer: CastContract.Transformer
) : ArchXDataPresenter<List<CharacterModel>, CastContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        CastContract.Presenter {

    private val colorPickerMap: MutableMap<Int, CharacterModel> = mutableMapOf()

    // region CastContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val character = item.data as? CharacterModel ?: return

        colorPickerMap[character.characterId] = character
        view?.showColorPicker(character.characterId, character.color)
    }

    override fun onItemValueChanged(item: Item.ViewModel, value: String?) {

        val character = item.data as? CharacterModel ?: return

        val updatedCharacter = character.copy(isHidden = value?.toBoolean() ?: false)

        dataSink.updateData(listOf(updatedCharacter))
    }

    override fun onColorPicked(colorPickerRequest: Int, @ColorInt color: Int) {
        val character = colorPickerMap[colorPickerRequest] ?: return

        val updatedCharacter = character.copy(color = color)

        dataSink.updateData(listOf(updatedCharacter))
    }
    // endregion
}
