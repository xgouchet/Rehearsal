package fr.xgouchet.rehearsal.script

import android.content.Context
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.ScriptModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ScriptDataSink(context: Context)
    : ScriptContract.ScriptDataSink {

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun createData(data: ScriptModel) {
    }

    override fun deleteData(data: ScriptModel) {
        val disposable = Single.just(data)
                .subscribeOn(Schedulers.io())
                .map { appDatabase.scriptDao().deleteById(it.scriptId) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Timber.i("#update @result:$it")
                        },
                        {
                            Timber.i(it, "#error #update")
                        }
                )

        disposables.add(disposable)
    }

    override fun updateData(data: ScriptModel) {
    }

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

}
