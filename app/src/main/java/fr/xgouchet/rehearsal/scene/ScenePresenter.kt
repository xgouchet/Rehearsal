package fr.xgouchet.rehearsal.scene

import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.data.ArchXDataPresenter
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.ui.Item

class ScenePresenter(owner: LifecycleOwner,
                     dataSource: SceneContract.DataSource,
                     dataSink: SceneContract.DataSink,
                     transformer: SceneContract.Transformer)
    : ArchXDataPresenter<List<CueWithCharacter>, SceneContract.View, List<Item.ViewModel>>(owner, dataSource, dataSink, transformer),
        SceneContract.Presenter {

    private var rawData : List<CueWithCharacter> = emptyList()

    override fun onItemSelected(item: Any) {

    }

    override fun onChanged(t: List<CueWithCharacter>) {
        rawData = t
        super.onChanged(t)
    }

    override fun onLinesVisibilityChanged(linesVisible: Boolean) {
        (transformer as? SceneContract.Transformer)?.setUserLinesVisible(linesVisible)
        onChanged(rawData)
    }

}
