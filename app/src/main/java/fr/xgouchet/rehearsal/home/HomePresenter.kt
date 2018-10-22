package fr.xgouchet.rehearsal.home

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import fr.xgouchet.rehearsal.ui.Item

class HomePresenter(owner: LifecycleOwner,
                    dataSource: ArchXDataSource<List<ScriptModel>>,
                    transformer: ArchXViewModelTransformer<List<ScriptModel>, List<Item.ViewModel>>)
    : ArchXDataPresenter<List<ScriptModel>, HomeContract.View, List<Item.ViewModel>>(owner, dataSource, transformer),
        HomeContract.Presenter
