package fr.xgouchet.rehearsal.home

import fr.xgouchet.rehearsal.core.NoOpDataSink
import fr.xgouchet.rehearsal.core.room.model.ScriptModel

class HomeDataSink
    : NoOpDataSink<List<ScriptModel>>(),
        HomeContract.DataSink
