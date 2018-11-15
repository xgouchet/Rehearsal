package fr.xgouchet.rehearsal.cast

import androidx.annotation.ColorInt
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.StableId

class CastPresenter(
        owner: LifecycleOwner,
        dataSource: CastContract.DataSource,
        dataSink: CastContract.DataSink,
        transformer: CastContract.Transformer
) : ArchXDataPresenter<List<CharacterModel>, CastContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        CastContract.Presenter {

    private val colorPickerMap: MutableMap<Int, CharacterModel> = mutableMapOf()

    private val enginePickerMap: MutableMap<Int, CharacterModel> = mutableMapOf()

    // region CastContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val character = item.getItemData() as? CharacterModel ?: return
        val subIndex = StableId.getSubIndex(item.getItemStableId())

        when (subIndex) {
            CastViewModelTransformer.IDX_COLOR -> {
                colorPickerMap[character.characterId] = character
                view?.showColorPicker(character.characterId, character.color)
            }
            CastViewModelTransformer.IDX_ENGINE -> {
                enginePickerMap[character.characterId] = character
                view?.showEnginePicker(character.characterId, character.ttsEngine)
            }
        }


    }

    override fun onItemValueChanged(item: Item.ViewModel, value: String?) {

        val character = item.getItemData() as? CharacterModel ?: return
        val subIndex = StableId.getSubIndex(item.getItemStableId())

        val updatedCharacter = when (subIndex) {
            CastViewModelTransformer.IDX_HIDE_LINES -> character.copy(isHidden = value?.toBoolean() ?: false)
            CastViewModelTransformer.IDX_PITCH -> character.copy(ttsPitch = value?.toFloat() ?: 0.5f)
            CastViewModelTransformer.IDX_RATE -> character.copy(ttsRate = value?.toFloat() ?: 0.5f)
            else -> null
        }


        if (updatedCharacter != null) {
            dataSink.updateData(listOf(updatedCharacter)) {
                if (it != null) {
                    view?.showError(it)
                }
            }
        }
    }

    override fun onColorPicked(requestId: Int, @ColorInt color: Int) {
        val character = colorPickerMap[requestId] ?: return

        val updatedCharacter = character.copy(color = color)

        dataSink.updateData(listOf(updatedCharacter)) {
            if (it != null) {
                view?.showError(it)
            }
        }
    }

    override fun onEnginePicked(requestId: Int, engine: String) {
        val character = enginePickerMap[requestId] ?: return

        val updatedCharacter = character.copy(ttsEngine = engine)

        dataSink.updateData(listOf(updatedCharacter)) {
            if (it != null) {
                view?.showError(it)
            }
        }
    }
    // endregion
}
