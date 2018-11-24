package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.room.AppDatabase

interface DbConverter<AM, DBM> {

    fun write(appModel: AM): DBM

    fun read(dataBaseModel: DBM, appDatabase: AppDatabase): AM
}
