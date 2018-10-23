package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.R
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

    override fun items(item: CueWithCharacter): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        val character = item.character
        val lastCharacter = lastCue?.character

        if (character != lastCharacter) {
            if (lastCue != null) {
                list.add(ItemDivider.ViewModel())
            }

            if (character != null) {
                val colorIdx = character.id % characterColors.size
                list.add(ItemCharacter.ViewModel(
                        characterName = character.name,
                        characterExtension = item.characterExtension,
                        foreground = characterColors[colorIdx]
                ))
            }
        }

        when (item.type) {
            CueModel.TYPE_DIALOG -> list.add(ItemDialog.ViewModel(
                    line = item.content,
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

    companion object {
        private val characterColors = listOf(
                R.color.character_fg_0,
                R.color.character_fg_1,
                R.color.character_fg_2,
                R.color.character_fg_3,
                R.color.character_fg_4,
                R.color.character_fg_5,
                R.color.character_fg_6,
                R.color.character_fg_7,
                R.color.character_fg_8,
                R.color.character_fg_9,
                R.color.character_fg_10,
                R.color.character_fg_11,
                R.color.character_fg_12,
                R.color.character_fg_13,
                R.color.character_fg_14,
                R.color.character_fg_15
        )
    }
}

