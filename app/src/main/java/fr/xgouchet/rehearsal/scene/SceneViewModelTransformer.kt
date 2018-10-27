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
import fr.xgouchet.rehearsal.ui.ItemLyrics

class SceneViewModelTransformer
    : PrincipledViewModelTransformer<CueWithCharacter, Item.ViewModel>(),
        SceneContract.Transformer {

    private var userLinesVisible = false
    private var lastCue: CueWithCharacter? = null

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this scene is empty"
                )
        )
    }

    override fun headers(appModel: List<CueWithCharacter>): Collection<Item.ViewModel> {
        lastCue = null
        return super.headers(appModel)
    }

    override fun transformItem(index: Int, item: CueWithCharacter): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        val character = item.character
        val lastCharacter = lastCue?.character
        val colorIndex = character?.id ?: -1

        if (character != lastCharacter) {
            if (lastCue != null) {
                list.add(ItemDivider.ViewModel())
            }

            if (character != null) {
                list.add(ItemCharacter.ViewModel(
                        characterName = character.name,
                        characterExtension = item.characterExtension,
                        colorIndex = colorIndex
                ))
            }
        }

        val hideCue = if (userLinesVisible) false else character?.isSelected ?: false
        val cueItem = when (item.type) {
            CueModel.TYPE_DIALOG -> ItemDialog.ViewModel(
                    line = item.content,
                    hidden = hideCue,
                    colorIndex = colorIndex,
                    data = item
            )

            CueModel.TYPE_ACTION -> ItemAction.ViewModel(
                    direction = item.content,
                    hidden = hideCue,
                    colorIndex = colorIndex,
                    data = item
            )

            CueModel.TYPE_LYRICS -> ItemLyrics.ViewModel(
                    lyrics = item.content,
                    hidden = hideCue,
                    colorIndex = colorIndex,
                    data = item
            )
            else -> null
        }
        cueItem?.let { list.add(it) }

        lastCue = item

        return list
    }


    override fun setUserLinesVisible(visible: Boolean) {
        userLinesVisible = visible
    }
}

