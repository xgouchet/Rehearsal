package fr.xgouchet.rehearsal.home

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

class HomePresenter(
        owner: LifecycleOwner,
        dataSource: HomeContract.DataSource,
        transformer: HomeContract.Transformer
) : ArchXDataPresenter<List<ScriptModel>, HomeContract.View, List<Item.ViewModel>>(owner, dataSource, transformer),
        HomeContract.Presenter {


    override fun onItemSelected(item: Item.ViewModel) {
        val script = item.getItemData() as? ScriptModel

        if (script != null) {
            view?.navigateToScript(script)
        }
    }
}
