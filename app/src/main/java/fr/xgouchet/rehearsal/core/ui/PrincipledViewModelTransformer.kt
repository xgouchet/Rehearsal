package fr.xgouchet.rehearsal.core.ui

import fr.xgouchet.archx.ArchXViewModelTransformer

abstract class PrincipledViewModelTransformer<AM, VM>
    : ArchXViewModelTransformer<List<AM>, List<VM>> {


    // region ArchXViewModelTransformer

    final override fun transform(appModel: List<AM>): List<VM> {
        val result = mutableListOf<VM>()

        if (appModel.isEmpty()) {
            result.addAll(empty())
        } else {
            result.addAll(headers(appModel))
            appModel.forEach {
                result.addAll(items(it))
            }
            result.addAll(footers(appModel))
        }

        return result
    }

    // endregion

    // region Open/Abstract

    open fun empty(): Collection<VM> = emptyList()

    open fun headers(appModel: List<AM>): Collection<VM> = emptyList()

    open fun items(item: AM): Collection<VM> = emptyList()

    open fun footers(appModel: List<AM>): Collection<VM> = emptyList()

    // endregion

}
