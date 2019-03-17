package fr.xgouchet.rehearsal.screen.cast

import androidx.annotation.ColorInt
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSink
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.archx.rx.schedule
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.StableId
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CastPresenter(
        dataSource: ArchXDataSource<List<Character>>,
        private val dataSink: ArchXDataSink<Character>,
        transformer: CastContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Character>, CastContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        CastContract.Presenter {

    private val colorPickerMap: MutableMap<Long, Character> = mutableMapOf()
    private val enginePickerMap: MutableMap<Long, Character> = mutableMapOf()

    private val editingCompositeDisposable = CompositeDisposable()

    // region ArchXDataPresenter

    override fun onViewDetached() {
        super.onViewDetached()
        editingCompositeDisposable.clear()
    }

    // endregion

    // region CastContract.Presenter

    override fun onItemSelected(item: Item.ViewModel) {
        val character = item.getItemData() as? Character ?: return
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

        val character = item.getItemData() as? Character ?: return
        val subIndex = StableId.getSubIndex(item.getItemStableId())

        val updatedCharacter = when (subIndex) {
            CastViewModelTransformer.IDX_HIDE_LINES -> character.copy(isHidden = value?.toBoolean() ?: false)
            CastViewModelTransformer.IDX_PITCH -> character.copy(ttsPitch = value?.toFloat() ?: 0.5f)
            CastViewModelTransformer.IDX_RATE -> character.copy(ttsRate = value?.toFloat() ?: 0.5f)
            else -> null
        }


        if (updatedCharacter != null) {
            updateCharacter(updatedCharacter)
        }
    }

    override fun onColorPicked(requestId: Long, @ColorInt color: Int) {
        val character = colorPickerMap[requestId] ?: return

        val updatedCharacter = character.copy(color = color)

        updateCharacter(updatedCharacter)
    }

    override fun onEnginePicked(requestId: Long, engine: String) {
        val character = enginePickerMap[requestId] ?: return

        val updatedCharacter = character.copy(ttsEngine = engine)

        updateCharacter(updatedCharacter)
    }

    // endregion

    // region Internal
    private fun updateCharacter(updatedCharacter: Character) {
        val disposable = dataSink.updateData(updatedCharacter)
                .schedule(schedulerProvider)
                .subscribe(
                        { Timber.i("#updated @character:$it") },
                        { view?.showError(it) }
                )
        editingCompositeDisposable.add(disposable)
    }
    // endregion
}
