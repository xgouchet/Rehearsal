package fr.xgouchet.rehearsal.scene

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_LONG_CLICK
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class SceneFragment
    : ItemListFragment(),
        SceneContract.View {


    private var linesVisible: Boolean = false
    private var hasBookmarks: Boolean = false
    private var cueMenuContext: CueMenuContext? = null

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

        menu.findItem(R.id.action_go_to_bookmark).isVisible = hasBookmarks

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_show_hide_lines -> {
                (presenter as? SceneContract.Presenter)?.onLinesVisibilityChanged(!linesVisible)
                true
            }
            R.id.action_go_to_bookmark -> {
                (presenter as? SceneContract.Presenter)?.onGoToBookmarkSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val context = cueMenuContext
        return if (context == null) {
            false
        } else {
            when (item.itemId) {
                R.id.action_bookmark_cue -> {
                    (presenter as? SceneContract.Presenter)?.onAddBookmarkPicked(context.cueId)
                    true
                }
                R.id.action_unbookmark -> {
                    (presenter as? SceneContract.Presenter)?.onRemoveBookmarkPicked(context.cueId)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }
    // endregion

    // region FragmentList

    override fun setupViews() {
        super.setupViews()
        registerForContextMenu(recyclerView)
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

    override fun showHasBookmarks(hasBookmarks: Boolean) {
        this.hasBookmarks = hasBookmarks
        activity?.invalidateOptionsMenu()
    }

    override fun showBookmarksDialog(bookmarks: List<Pair<Int, String>>) {
        val currentContext = context ?: return
        showBookmarksDialog(currentContext, bookmarks)
    }

    override fun showContextMenu(context: CueMenuContext) {
        this.cueMenuContext = context
        recyclerView?.let { CueMenuHelper(context).openContextMenu(it) }
    }

    override fun scrollToRow(index: Int) {
        recyclerView?.smoothScrollToPosition(index)
    }
    // endregion

    // region Internal

    private fun showBookmarksDialog(context: Context,
                                    bookmarks: List<Pair<Int, String>>) {
        val bookmarkValues = bookmarks.map { it.second }.toTypedArray()
        val builder = AlertDialog.Builder(context)
                .setTitle(R.string.scene_prompt_bookmark)
                .setItems(bookmarkValues) { d, w ->
                    val bookmarkId = bookmarks[w].first
                    (presenter as? SceneContract.Presenter)?.onBookmarkPicked(bookmarkId)
                    d.dismiss()
                }


        val dialog = builder.create()
        dialog.show()
    }

    // endregion
}
