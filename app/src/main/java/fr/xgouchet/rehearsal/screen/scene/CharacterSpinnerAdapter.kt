package fr.xgouchet.rehearsal.screen.scene

import android.content.Context
import android.widget.ArrayAdapter

class CharacterSpinnerAdapter(
        context: Context,
        private val characters: List<Pair<Int, String>>
) : ArrayAdapter<Pair<Int, String>>(context, android.R.layout.simple_spinner_item) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

}
