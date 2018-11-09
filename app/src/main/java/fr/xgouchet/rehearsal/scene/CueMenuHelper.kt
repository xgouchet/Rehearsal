package fr.xgouchet.rehearsal.scene

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.ContextMenuHelper

class CueMenuHelper(data: CueMenuContext) : ContextMenuHelper<CueMenuContext>(data) {

    override fun onCreateContextMenu(menu: ContextMenu,
                                     v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo?) {
        val menuInflater = MenuInflater(v.context)
        menuInflater.inflate(R.menu.cue_context, menu)

        menu.findItem(R.id.action_bookmark_cue).isVisible = !data.isBookmarked
        menu.findItem(R.id.action_unbookmark).isVisible = data.isBookmarked
    }


}
