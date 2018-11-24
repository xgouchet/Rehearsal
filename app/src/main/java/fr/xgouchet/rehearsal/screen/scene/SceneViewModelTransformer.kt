package fr.xgouchet.rehearsal.screen.scene

import fr.xgouchet.rehearsal.core.model.Cue
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
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
    : PrincipledViewModelTransformer<Cue, Item.ViewModel>(),
        SceneContract.Transformer {

    private var userLinesVisible = false

    private var lastCue: Cue? = null
    private var activeCueId : Long = -1

    override fun empty(): Collection<Item.ViewModel> {
        return listOf(
                ItemEmpty.ViewModel(
                        id = StableId.getStableId(0, 0, Item.Type.EMPTY.ordinal),
                        title = "¯\\_(ツ)_/¯",
                        body = "It seems this scene is empty"
                )
        )
    }

    override fun headers(appModel: List<Cue>): Collection<Item.ViewModel> {
        lastCue = null
        return super.headers(appModel)
    }

    override fun transformItem(index: Int, item: Cue): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        val character = item.character
        val lastCharacter = lastCue?.character
        val color = CharacterColor.get(character)

        val highlightCue = item.cueId == activeCueId
        val highlightColor = if (highlightCue) CharacterColor.getHighlight(character) else null

        if (character != lastCharacter) {
            if (lastCue != null) {
                list.add(ItemDivider.ViewModel(id = StableId.getStableId(item.position, 0, Item.Type.DIVIDER.ordinal)))
            }

            if (character != null) {
                list.add(ItemCharacter.ViewModel(
                        id = StableId.getStableId(item.position, 1, Item.Type.CHARACTER.ordinal),
                        characterName = character.name,
                        characterExtension = item.characterExtension,
                        color = color,
                        data = item
                ))
            }
        }

        val hideCue = if (userLinesVisible) false else character?.isHidden ?: false
        val cueItem = when (item.type) {
            CueDbModel.TYPE_DIALOG -> ItemDialog.ViewModel(
                    id = StableId.getStableId(item.position, 2, Item.Type.DIALOG.ordinal),
                    line = item.content,
                    hidden = hideCue,
                    color = color,
                    highlightColor = highlightColor,
                    hasBookmark = item.isBookmarked,
                    hasNote = !item.note.isNullOrBlank(),
                    data = item
            )

            CueDbModel.TYPE_ACTION -> ItemAction.ViewModel(
                    id = StableId.getStableId(item.position, 2, Item.Type.ACTION.ordinal),
                    direction = item.content,
                    hidden = hideCue,
                    color = color,
                    hasBookmark = item.isBookmarked,
                    hasNote = !item.note.isNullOrBlank(),
                    data = item
            )

            CueDbModel.TYPE_LYRICS -> ItemLyrics.ViewModel(
                    id = StableId.getStableId(item.position, 2, Item.Type.LYRICS.ordinal),
                    lyrics = item.content,
                    hidden = hideCue,
                    color = color,
                    highlightColor = highlightColor,
                    hasBookmark = item.isBookmarked,
                    hasNote = !item.note.isNullOrBlank(),
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

    override fun setSelectedCue(cueId: Long) {
        activeCueId = cueId
    }
}

