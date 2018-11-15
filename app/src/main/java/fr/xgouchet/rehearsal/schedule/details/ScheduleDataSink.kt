package fr.xgouchet.rehearsal.schedule.details

import android.content.Context
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ScheduleDataSink(context: Context)
    : ScheduleRangesContract.ScheduleDataSink {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun createData(data: ScheduleModel, onEnd: (Throwable?) -> Unit) {
    }

    override fun updateData(data: ScheduleModel, onEnd: (Throwable?) -> Unit) {
    }

    override fun deleteData(data: ScheduleModel, onEnd: (Throwable?) -> Unit) {
        val disposable = Single.just(data)
                .subscribeOn(Schedulers.io())
                .map { appDatabase.scheduleDao().deleteById(it.scheduleId) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Timber.i("#deleteById @result:$it")
                            onEnd(null)
                        },
                        {
                            Timber.i(it, "#error #deleteById")
                            onEnd(it)
                        }
                )

        disposables.add(disposable)
    }
}
