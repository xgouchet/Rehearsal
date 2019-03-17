package fr.xgouchet.rehearsal.screen.props

import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.archx.rx.SchedulerProvider
import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.ui.Item

class PropsPresenter(
        dataSource: ArchXDataSource<List<Prop>>,
        transformer: PropsContract.Transformer,
        schedulerProvider: SchedulerProvider
) : ArchXDataPresenter<List<Prop>, PropsContract.View, List<Item.ViewModel>>(dataSource, transformer, schedulerProvider),
        PropsContract.Presenter {

}
