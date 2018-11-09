package fr.xgouchet.rehearsal.scene

import fr.xgouchet.rehearsal.core.NoOpDataSink
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter

class SceneDataSink
    : NoOpDataSink<List<CueWithCharacter>>(),
        SceneContract.DataSink
