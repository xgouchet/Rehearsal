package fr.xgouchet.rehearsal.cast

import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemCharacter
import fr.xgouchet.rehearsal.ui.ItemDivider
import fr.xgouchet.rehearsal.ui.ItemSwitch
import fr.xgouchet.rehearsal.ui.StableId

class CastViewModelTransformer
    : PrincipledViewModelTransformer<CharacterModel, Item.ViewModel>(),
        CastContract.Transformer {

    override fun transformItem(index: Int, item: CharacterModel): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        if (index > 0) {
            list.add(ItemDivider.ViewModel(id = StableId.getStableId(index, 0, Item.Type.DIVIDER.ordinal)))
        }

        list.add(ItemCharacter.ViewModel(
                id = StableId.getStableId(index, 1, Item.Type.CHARACTER.ordinal),
                characterName = item.name,
                colorIndex = item.characterId,
                data = item
        ))

        list.add(ItemSwitch.ViewModel(
                id = StableId.getStableId(index, 2, Item.Type.SWITCH.ordinal),
                labelRes = R.string.cast_action_myRole,
                value = item.isHidden,
                data = item
        ))

        return list
    }
}
