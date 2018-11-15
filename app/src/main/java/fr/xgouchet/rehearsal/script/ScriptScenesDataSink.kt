package fr.xgouchet.rehearsal.script

import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.room.join.SceneWithCount

class ScriptScenesDataSink
    : ArchXNoOpDataSink<List<SceneWithCount>>(),
        ScriptContract.DataSink
