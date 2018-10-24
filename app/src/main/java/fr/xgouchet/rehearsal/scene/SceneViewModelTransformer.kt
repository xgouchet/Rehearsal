package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemAction
import fr.xgouchet.rehearsal.ui.ItemCharacter
import fr.xgouchet.rehearsal.ui.ItemDialog
import fr.xgouchet.rehearsal.ui.ItemDivider
import fr.xgouchet.rehearsal.ui.ItemEmpty

class SceneViewModelTransformer
    : PrincipledViewModelTransformer<CueWithCharacter, Item.ViewModel>(),
        SceneContract.Transformer {

    private var lastCue: CueWithCharacter? = null

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this scene is empty"
                )
        )
    }

    override fun transformItem(index: Int, item: CueWithCharacter): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        val character = item.character
        val lastCharacter = lastCue?.character

        if (character != lastCharacter) {
            if (lastCue != null) {
                list.add(ItemDivider.ViewModel())
            }

            if (character != null) {
                list.add(ItemCharacter.ViewModel(
                        characterName = character.name,
                        characterExtension = item.characterExtension,
                        colorIndex = character.id
                ))
            }
        }

        when (item.type) {
            CueModel.TYPE_DIALOG -> list.add(ItemDialog.ViewModel(
                    line = item.content,
                    hidden = character?.isSelected ?: false,
                    data = item
            ))

            CueModel.TYPE_ACTION -> list.add(ItemAction.ViewModel(
                    direction = item.content,
                    data = item
            ))
        }

        lastCue = item

        return list
    }


}

