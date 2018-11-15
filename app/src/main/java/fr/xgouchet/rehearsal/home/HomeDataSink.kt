package fr.xgouchet.rehearsal.home

import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.room.model.ScriptModel

class HomeDataSink
    : ArchXNoOpDataSink<List<ScriptModel>>(),
        HomeContract.DataSink
