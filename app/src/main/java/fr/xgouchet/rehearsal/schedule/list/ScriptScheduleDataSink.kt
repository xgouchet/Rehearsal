package fr.xgouchet.rehearsal.schedule.list

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel

class ScriptScheduleDataSink(context: Context)
    : ArchXNoOpDataSink<List<ScheduleModel>>(),
        ScriptScheduleContract.DataSink {

}
