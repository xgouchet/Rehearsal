package fr.xgouchet.rehearsal.script

import fr.xgouchet.rehearsal.core.NoOpDataSink
import fr.xgouchet.rehearsal.core.room.model.SceneModel

class ScriptScenesDataSink
    : NoOpDataSink<List<SceneModel>>(),
        ScriptContract.DataSink
