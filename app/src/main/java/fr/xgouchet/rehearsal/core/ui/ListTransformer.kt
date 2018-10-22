package fr.xgouchet.rehearsal.core.ui

import fr.xgouchet.archx.ArchXViewModelTransformer

class ListTransformer<AM, VM>(private val delegate: ArchXViewModelTransformer<AM, VM>)
    : ArchXViewModelTransformer<List<AM>, List<VM>> {

    override fun transform(appModel: List<AM>): List<VM> {
        return appModel.asSequence()
                .map { delegate.transform(it) }
                .toList()
    }
}