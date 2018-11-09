package fr.xgouchet.rehearsal.ui

import android.view.ContextMenu
import android.view.View

abstract class ContextMenuHelper<T>(val data: T)
    : View.OnCreateContextMenuListener {

    fun openContextMenu(view: View) {
        view.setOnCreateContextMenuListener(this)
        view.showContextMenu()
        view.setOnCreateContextMenuListener(null)
    }

}
