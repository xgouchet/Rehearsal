package fr.xgouchet.rehearsal.screen.scene

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.ContextMenuHelper

class CueMenuHelper(data: CueInfo) : ContextMenuHelper<CueInfo>(data) {

    override fun onCreateContextMenu(menu: ContextMenu,
                                     v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo?) {
        val menuInflater = MenuInflater(v.context)
        menuInflater.inflate(R.menu.cue_context, menu)

        menu.findItem(R.id.action_add_bookmark).isVisible = !data.isBookmarked
        menu.findItem(R.id.action_remove_bookmark).isVisible = data.isBookmarked

        menu.findItem(R.id.action_add_note).isVisible = !data.hasNote
        menu.findItem(R.id.action_note).isVisible = data.hasNote

        menu.findItem(R.id.action_add_prop).isVisible = !data.hasProps
        menu.findItem(R.id.action_props).isVisible = data.hasProps

        menu.setHeaderTitle(data.abstract)
        menu.setHeaderIcon(R.drawable.ic_cue_dialog)
    }


}
