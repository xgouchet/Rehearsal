package fr.xgouchet.rehearsal.cast

import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemCharacter
import fr.xgouchet.rehearsal.ui.ItemDivider
import fr.xgouchet.rehearsal.ui.ItemSwitch

class CastViewModelTransformer
    : PrincipledViewModelTransformer<CharacterModel, Item.ViewModel>(),
        CastContract.Transformer {


    override fun transformItem(index: Int, item: CharacterModel): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        if (index > 0) {
            list.add(ItemDivider.ViewModel())
        }

        list.add(ItemCharacter.ViewModel(
                characterName = item.name,
                colorIndex = item.id,
                data = item
        ))

        list.add(ItemSwitch.ViewModel(
                labelRes = R.string.cast_action_myRole,
                value = item.isSelected,
                data = item
        ))

        return list
    }
}
