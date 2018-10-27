package fr.xgouchet.rehearsal.cast

import android.content.Context
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CastDataSink(context: Context)
    : CastContract.DataSink {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun updateData(data: List<CharacterModel>) {
        val disposable = Observable.fromIterable(data)
                .subscribeOn(Schedulers.io())
                .delay(250, TimeUnit.MILLISECONDS)
                .map { appDatabase.characterDao().update(it) }
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

    override fun createData(data: List<CharacterModel>) {

    }

    override fun deleteData(data: List<CharacterModel>) {

    }

}
