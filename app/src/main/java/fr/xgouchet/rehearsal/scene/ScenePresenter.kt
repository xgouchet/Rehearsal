package fr.xgouchet.rehearsal.scene

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

class ScenePresenter(owner: LifecycleOwner,
                     cueDataSource: SceneContract.DataSource,
                     transformer: SceneContract.Transformer)
    : ArchXDataPresenter<List<CueWithCharacter>, SceneContract.View, List<Item.ViewModel>>(owner, cueDataSource, transformer),
        SceneContract.Presenter {

    override fun onItemSelected(item: Any) {

    }

}
