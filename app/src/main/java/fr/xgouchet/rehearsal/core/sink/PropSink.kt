package fr.xgouchet.rehearsal.core.sink

import android.content.Context
import fr.xgouchet.archx.data.ArchXNoOpDataSink
import fr.xgouchet.rehearsal.core.converter.PropDbConverter
import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.core.room.AppDatabase
import io.reactivex.Single

class PropSink(context: Context) : ArchXNoOpDataSink<Prop>() {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val propDbConverter = PropDbConverter()


    override fun createData(data: Prop): Single<Prop> {
        return Single.just(data)
                .map {
                    val dbModel = propDbConverter.write(it)
                    val id = appDatabase.propDao().insert(dbModel)
                    it.copy(propId = id)
                }
    }

}
