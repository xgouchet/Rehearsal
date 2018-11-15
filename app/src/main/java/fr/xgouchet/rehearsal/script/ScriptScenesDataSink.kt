package fr.xgouchet.rehearsal.script

import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.room.model.SceneModel

class ScriptScenesDataSink
    : ArchXNoOpDataSink<List<SceneModel>>(),
        ScriptContract.DataSink
