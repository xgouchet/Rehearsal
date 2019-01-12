package fr.xgouchet.rehearsal.screen.cast

import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.model.Character
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.CharacterColor
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemCharacter
import fr.xgouchet.rehearsal.ui.ItemColorPicker
import fr.xgouchet.rehearsal.ui.ItemDivider
import fr.xgouchet.rehearsal.ui.ItemInteractive
import fr.xgouchet.rehearsal.ui.ItemSlider
import fr.xgouchet.rehearsal.ui.ItemSwitch
import fr.xgouchet.rehearsal.ui.StableId

class CastViewModelTransformer
    : PrincipledViewModelTransformer<Character, Item.ViewModel>(),
        CastContract.Transformer {

    override fun transformItem(index: Int, item: Character): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        if (index > 0) {
            list.add(ItemDivider.ViewModel(id = StableId.getStableId(index, 0, Item.Type.DIVIDER.ordinal)))
        }


        val color = CharacterColor.get(item)

        list.add(
                ItemCharacter.ViewModel(
                        id = StableId.getStableId(index, 1, Item.Type.CHARACTER.ordinal),
                        characterName = item.name,
                        characterExtension = "(${item.cuesCount})",
                        color = color,
                        data = item
                )
        )

        list.add(
                ItemSwitch.ViewModel(
                        id = StableId.getStableId(index, IDX_HIDE_LINES, Item.Type.SWITCH.ordinal),
                        labelRes = R.string.cast_action_hideLines,
                        value = item.isHidden,
                        data = item
                )
        )

        list.add(
                ItemColorPicker.ViewModel(
                        id = StableId.getStableId(index, IDX_COLOR, Item.Type.COLOR.ordinal),
                        labelRes = R.string.cast_action_color,
                        color = color,
                        data = item
                )
        )
        list.add(
                ItemInteractive.ViewModel(
                        id = StableId.getStableId(index, IDX_ENGINE, Item.Type.INTERACTIVE.ordinal),
                        labelRes = R.string.cast_action_voiceEngine,
                        value = item.ttsEngine.orEmpty(),
                        data = item
                )
        )
        list.add(
                ItemSlider.ViewModel(
                        id = StableId.getStableId(index, IDX_PITCH, Item.Type.COLOR.ordinal),
                        labelRes = R.string.cast_action_voicePitch,
                        value = item.ttsPitch,
                        min = 0.5f,
                        max = 2.0f,
                        steps = 15,
                        data = item
                )
        )

        list.add(
                ItemSlider.ViewModel(
                        id = StableId.getStableId(index, IDX_RATE, Item.Type.COLOR.ordinal),
                        labelRes = R.string.cast_action_voiceRate,
                        value = item.ttsRate,
                        min = 0.5f,
                        max = 2.0f,
                        steps = 15,
                        data = item
                )
        )

        return list
    }

    companion object {
        const val IDX_HIDE_LINES = 2
        const val IDX_COLOR = 3
        const val IDX_ENGINE = 4
        const val IDX_PITCH = 5
        const val IDX_RATE = 6
    }
}
