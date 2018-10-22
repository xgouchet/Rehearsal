package fr.xgouchet.rehearsal.home

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.ScriptModel
import fr.xgouchet.rehearsal.core.ui.script.ScriptViewModel

class HomePresenter(owner: LifecycleOwner,
                    dataSource: ArchXDataSource<List<ScriptModel>>,
                    transformer: ArchXViewModelTransformer<List<ScriptModel>, List<ScriptViewModel>>)
    : ArchXDataPresenter<List<ScriptModel>, HomeContract.View, List<ScriptViewModel>>(owner, dataSource, transformer),
        HomeContract.Presenter {

}