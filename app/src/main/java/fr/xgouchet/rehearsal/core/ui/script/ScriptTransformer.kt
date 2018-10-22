package fr.xgouchet.rehearsal.core.ui.script

import fr.xgouchet.archx.ArchXViewModelTransformer
import fr.xgouchet.rehearsal.core.room.ScriptModel

class ScriptTransformer : ArchXViewModelTransformer<ScriptModel, ScriptViewModel> {

    override fun transform(appModel: ScriptModel): ScriptViewModel {
        return ScriptViewModel(title = appModel.title, author = appModel.author)
    }
}