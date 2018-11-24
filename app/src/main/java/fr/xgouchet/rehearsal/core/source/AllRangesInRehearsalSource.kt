package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.converter.RangeDbConverter
import fr.xgouchet.rehearsal.core.model.Range
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class AllRangesInRehearsalSource(
        context: Context,
        private val rehearsalId: Long
) : ArchXDataSource<List<Range>> {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val rangeDbConverter = RangeDbConverter()

    override fun getData(): Single<List<Range>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Range>> {
        return appDatabase.rangeDao()
                .getAllInRehearsal(rehearsalId)
                .map { list -> list.map { rangeDbConverter.read(it, appDatabase) } }
                .onBackpressureLatest()
    }

}
