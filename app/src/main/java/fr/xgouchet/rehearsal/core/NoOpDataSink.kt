package fr.xgouchet.rehearsal.core

import fr.xgouchet.archx.data.ArchXDataSink

open class NoOpDataSink<T>
    : ArchXDataSink<T> {

    override fun createData(data: T) {}

    override fun deleteData(data: T) {}

    override fun updateData(data: T) {}
}
