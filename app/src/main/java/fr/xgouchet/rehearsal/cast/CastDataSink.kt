package fr.xgouchet.rehearsal.cast

import android.content.Context
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CastDataSink(context: Context)
    : CastContract.DataSink {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun updateData(data: List<CharacterModel>, onEnd: (Throwable?) -> Unit) {
        val disposable = Observable.fromIterable(data)
                .subscribeOn(Schedulers.io())
                .map { appDatabase.characterDao().update(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Timber.i("#update @result:$it")
                            onEnd(null)
                        },
                        {
                            Timber.i(it, "#error #update")
                            onEnd(it)
                        }
                )

        disposables.add(disposable)
    }

    override fun createData(data: List<CharacterModel>, onEnd: (Throwable?) -> Unit) {
    }

    override fun deleteData(data: List<CharacterModel>, onEnd: (Throwable?) -> Unit) {
    }

}
