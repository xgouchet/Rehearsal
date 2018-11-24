package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Single

class SingleScriptSink(context: Context) : ArchXNoOpDataSink<Script>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    override fun deleteData(data: Script): Single<Script> {
        return Single.fromCallable {
            appDatabase.scriptDao().deleteById(data.scriptId)
        }.map { data }
    }

}
