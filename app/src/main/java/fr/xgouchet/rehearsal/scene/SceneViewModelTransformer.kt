package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.CharacterColor
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemAction
import fr.xgouchet.rehearsal.ui.ItemCharacter
import fr.xgouchet.rehearsal.ui.ItemDialog
import fr.xgouchet.rehearsal.ui.ItemDivider
import fr.xgouchet.rehearsal.ui.ItemEmpty
import fr.xgouchet.rehearsal.ui.ItemLyrics
import fr.xgouchet.rehearsal.ui.StableId

class SceneViewModelTransformer
    : PrincipledViewModelTransformer<CueWithCharacter, Item.ViewModel>(),
        SceneContract.Transformer {

    private var userLinesVisible = false

    private var lastCue: CueWithCharacter? = null
    private var activeCueId: Int = -1

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
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
        val color = CharacterColor.get(character)

        if (character != lastCharacter) {
            if (lastCue != null) {
                list.add(ItemDivider.ViewModel(id = StableId.getStableId(index, 0, Item.Type.DIVIDER.ordinal)))
            }

            if (character != null) {
                list.add(ItemCharacter.ViewModel(
                        id = StableId.getStableId(index, 1, Item.Type.CHARACTER.ordinal),
                        characterName = character.name,
                        characterExtension = item.characterExtension,
                        color = color,
                        data = item
                ))
            }
        }

        val hideCue = if (userLinesVisible) false else character?.isHidden ?: false
        val highlightCue = item.cueId == activeCueId
        val cueItem = when (item.type) {
            CueModel.TYPE_DIALOG -> ItemDialog.ViewModel(
                    id = StableId.getStableId(index, 2, Item.Type.DIALOG.ordinal),
                    line = item.content,
                    hidden = hideCue,
                    color = color,
                    highlight = highlightCue,
                    hasBookmark = item.isBookmarked,
                    hasNote = !item.note.isNullOrBlank(),
                    data = item
            )

            CueModel.TYPE_ACTION -> ItemAction.ViewModel(
                    id = StableId.getStableId(index, 2, Item.Type.ACTION.ordinal),
                    direction = item.content,
                    hidden = hideCue,
                    color = color,
                    data = item
            )

            CueModel.TYPE_LYRICS -> ItemLyrics.ViewModel(
                    id = StableId.getStableId(index, 2, Item.Type.LYRICS.ordinal),
                    lyrics = item.content,
                    hidden = hideCue,
                    color = color,
                    highlight = highlightCue,
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

    override fun setSelectedCue(cueId: Int) {
        activeCueId = cueId
    }
}

