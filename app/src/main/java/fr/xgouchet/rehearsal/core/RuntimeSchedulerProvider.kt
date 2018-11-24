package fr.xgouchet.rehearsal.core

import fr.xgouchet.archx.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RuntimeSchedulerProvider : SchedulerProvider {

    override fun observeOn(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun subscribeOn(): Scheduler {
        return Schedulers.io()
    }
}
