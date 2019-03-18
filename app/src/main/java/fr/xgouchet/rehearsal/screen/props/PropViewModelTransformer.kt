package fr.xgouchet.rehearsal.screen.props

import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemProp
import fr.xgouchet.rehearsal.ui.StableId

class PropViewModelTransformer
    : PrincipledViewModelTransformer<Prop, Item.ViewModel>(),
        PropsContract.Transformer {

    override fun transformItem(index: Int, item: Prop): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        val scenes = item.scenes.joinToString(
                separator = "\n"
        ) { " - ${it.title}" }

        list.add(
                ItemProp.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.PROP.ordinal),
                        title = "${item.name}\n$scenes",
                        data = item
                )
        )

        return list
    }

}
