package fr.xgouchet.rehearsal.core.source

import android.content.Context
import fr.xgouchet.archx.data.ArchXDataSource
import fr.xgouchet.rehearsal.core.converter.PropDbConverter
import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class AllPropsInScriptSource(
        context: Context,
        val scriptId: Long
) : ArchXDataSource<List<Prop>> {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val propDbConverter = PropDbConverter()

    override fun getData(): Single<List<Prop>> {
        return listenData().firstOrError()
    }

    override fun listenData(): Flowable<List<Prop>> {
        return appDatabase.propDao()
                .getAllFromScript(scriptId)
                .map { list -> list.map { propDbConverter.read(it, appDatabase) } }
                .onBackpressureLatest()
    }

}
