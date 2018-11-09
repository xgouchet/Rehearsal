package fr.xgouchet.rehearsal.scene

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_LONG_CLICK
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class SceneFragment
    : ItemListFragment(),
        SceneContract.View {


    private var linesVisible: Boolean = false

    // region Fragment (OptionsMenu)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.scene, menu)

        menu.findItem(R.id.action_show_hide_lines).apply {
            setIcon(if (linesVisible) R.drawable.ic_hide_lines else R.drawable.ic_show_lines)
            setTitle(if (linesVisible) R.string.menu_hideLines else R.string.menu_showLines)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_show_hide_lines -> {
                (presenter as? SceneContract.Presenter)?.onLinesVisibilityChanged(!linesVisible)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        var consumed = true
        when (action) {
            ACTION_DEFAULT -> (presenter as? SceneContract.Presenter)?.onItemSelected(item)
            ACTION_LONG_CLICK -> (presenter as? SceneContract.Presenter)?.onItemPressed(item)

            else -> consumed = false
        }

        return consumed
    }

    // endregion

    // region SceneContract.View

    override fun showLinesVisible(linesVisible: Boolean) {
        this.linesVisible = linesVisible
        activity?.invalidateOptionsMenu()
    }

    override fun showReading(reading: Boolean) {
        (activity as? SceneActivity)?.showReading(reading)
    }

    override fun scrollToRow(index: Int) {
        recyclerView?.smoothScrollToPosition(index)
    }
    // endregion

}
