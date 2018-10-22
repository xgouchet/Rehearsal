package fr.xgouchet.rehearsal.core.ui.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import fr.xgouchet.archx.ui.ArchXAdapter
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.ui.script.ScriptViewModel

class EditorAdapter
    : ArchXAdapter<EditorViewModel, EditorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_script, parent, false)
        return EditorViewHolder(view)
    }
}