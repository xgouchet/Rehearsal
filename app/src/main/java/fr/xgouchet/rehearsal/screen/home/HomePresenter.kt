package fr.xgouchet.rehearsal.screen.home

import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.ui.Item

class HomePresenter(
        dataSource: ArchXDataSource<List<Script>>,
        transformer: HomeContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Script>, HomeContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        HomeContract.Presenter {


    override fun onItemSelected(item: Item.ViewModel) {
        val script = item.getItemData() as? Script

        if (script != null) {
            view?.navigateToScript(script)
        }
    }

    override fun onErrorLoadingData(it: Throwable) {
        view?.showError(it)
    }
}
