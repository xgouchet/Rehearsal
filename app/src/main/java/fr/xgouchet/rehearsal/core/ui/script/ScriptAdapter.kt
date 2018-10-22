package fr.xgouchet.rehearsal.core.ui.script

import android.view.LayoutInflater
import android.view.ViewGroup
import fr.xgouchet.archx.ui.ArchXAdapter
import fr.xgouchet.rehearsal.R

class ScriptAdapter
    : ArchXAdapter<ScriptViewModel, ScriptViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_script, parent, false)

        return ScriptViewHolder(view)
    }
}