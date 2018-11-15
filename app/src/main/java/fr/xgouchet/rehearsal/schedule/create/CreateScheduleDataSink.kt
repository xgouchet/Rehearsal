package fr.xgouchet.rehearsal.schedule.create

import android.content.Context
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CreateScheduleDataSink(context: Context)
    : CreateScheduleContract.DataSink {

    val appDatabase = AppDatabase.getInstance(context)
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun createData(data: Pair<ScheduleModel, List<RangeModel>>, onEnd: (Throwable?) -> Unit) {
        val disposable = Observable.just(data)
                .subscribeOn(Schedulers.io())
                .map { pair ->
                    val id = appDatabase.scheduleDao().insert(pair.first).toInt()
                    pair.second.forEach() {
                        val updated = it.copy(scheduleId = id)
                        appDatabase.rangeDao().insert(updated)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Timber.i("#insert @result:$it")
                            onEnd(null)
                        },
                        {
                            Timber.i(it, "#error #insert")
                            onEnd(it)
                        }
                )

        disposables.add(disposable)
    }


    override fun updateData(data: Pair<ScheduleModel, List<RangeModel>>, onEnd: (Throwable?) -> Unit) {
    }

    override fun deleteData(data: Pair<ScheduleModel, List<RangeModel>>, onEnd: (Throwable?) -> Unit) {
    }


}
