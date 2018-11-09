package fr.xgouchet.rehearsal.scene

import android.content.Context
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SceneDataSink(context: Context)
    : SceneContract.DataSink {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun updateData(data: List<CueWithCharacter>) {
        val disposable = Observable.fromIterable(data)
                .subscribeOn(Schedulers.io())
                .map { appDatabase.cueDao().update(it.asCueModel()) }
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

    override fun createData(data: List<CueWithCharacter>) {

    }

    override fun deleteData(data: List<CueWithCharacter>) {

    }
}
