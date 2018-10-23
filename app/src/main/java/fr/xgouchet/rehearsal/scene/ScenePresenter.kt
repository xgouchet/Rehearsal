package fr.xgouchet.rehearsal.scene

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

class ScenePresenter(owner: LifecycleOwner,
                     dataSource: SceneContract.DataSource,
                     transformer: SceneContract.Transformer)
    : ArchXDataPresenter<List<CueWithCharacter>, SceneContract.View, List<Item.ViewModel>>(owner, dataSource, transformer),
        SceneContract.Presenter {

    override fun onItemSelected(item: Any) {
       //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
