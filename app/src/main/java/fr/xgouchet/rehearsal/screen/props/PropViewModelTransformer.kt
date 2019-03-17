package fr.xgouchet.rehearsal.screen.props

import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.core.ui.PrincipledViewModelTransformer
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemProp
import fr.xgouchet.rehearsal.ui.ItemScene
import fr.xgouchet.rehearsal.ui.StableId

class PropViewModelTransformer
    : PrincipledViewModelTransformer<Prop, Item.ViewModel>(),
        PropsContract.Transformer {

    override fun transformItem(index: Int, item: Prop): Collection<Item.ViewModel> {
        val list = mutableListOf<Item.ViewModel>()

        list.add(
                ItemProp.ViewModel(
                        id = StableId.getStableId(index, 0, Item.Type.PROP.ordinal),
                        title = item.name,
                        data = item
                )
        )

        item.scenes.forEach { scene ->
            list.add(
                    ItemScene.ViewModel(
                            id = StableId.getStableId(index, scene.position, Item.Type.SCENE.ordinal),
                            title = scene.title,
                            cuesCount = scene.cuesCount,
                            data = scene
                    )
            )
        }

        return list
    }

}
