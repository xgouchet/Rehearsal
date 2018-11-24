package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.converter.RangeDbConverter
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Single

class RangeSink(context: Context)
    : ArchXNoOpDataSink<Range>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private val rangeDbConverter = RangeDbConverter()

    override fun createData(data: Range): Single<Range> {
        return Single.fromCallable {

            val dbModel = rangeDbConverter.write(data)
            val id = appDatabase.rangeDao().insert(dbModel)
            data.copy(rangeId = id)
        }
    }
}
