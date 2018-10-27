package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.core.NoOpDataSink
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter

class SceneDataSink
    : NoOpDataSink<List<CueWithCharacter>>(),
        SceneContract.DataSink
