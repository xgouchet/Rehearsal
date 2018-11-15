package fr.xgouchet.rehearsal.scene

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_LONG_CLICK
import fr.xgouchet.rehearsal.ui.ACTION_NOTE
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class SceneFragment
    : ItemListFragment(),
        SceneContract.View {


    private var linesVisible: Boolean = false
    private var hasBookmarks: Boolean = false
    private var cueInfo: CueInfo? = null

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

        val context = cueInfo
        return if (context == null) {
            false
        } else {
            when (item.itemId) {

                R.id.action_edit_cue -> {
                    (presenter as? SceneContract.Presenter)?.onEditCuePicked(context.cueId)
                    true
                }

                R.id.action_add_bookmark -> {
                    (presenter as? SceneContract.Presenter)?.onAddBookmarkPicked(context.cueId)
                    true
                }
                R.id.action_remove_bookmark -> {
                    (presenter as? SceneContract.Presenter)?.onRemoveBookmarkPicked(context.cueId)
                    true
                }

                R.id.action_add_note -> {
                    (presenter as? SceneContract.Presenter)?.onAddNotePicked(context.cueId)
                    true
                }
                R.id.action_show_note -> {
                    (presenter as? SceneContract.Presenter)?.onShowNotePicked(context.cueId)
                    true
                }
                R.id.action_edit_note -> {
                    (presenter as? SceneContract.Presenter)?.onEditNotePicked(context.cueId)
                    true
                }
                R.id.action_remove_note -> {
                    (presenter as? SceneContract.Presenter)?.onRemoveNotesPicked(context.cueId)
                    true
                }

                R.id.action_delete_cue -> {
                    (presenter as? SceneContract.Presenter)?.onDeleteCue(context.cueId)
                    true
                }
                R.id.action_add_dialog -> {
                    (presenter as? SceneContract.Presenter)?.onAddDialog(context.cueId)
                    true
                }
                R.id.action_add_action -> {
                    (presenter as? SceneContract.Presenter)?.onAddAction(context.cueId)
                    true
                }
                R.id.action_add_lyrics -> {
                    (presenter as? SceneContract.Presenter)?.onAddLyrics(context.cueId)
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
            ACTION_NOTE -> {
                val cue = item.getItemData() as? CueWithCharacter
                if (cue != null) {
                    (presenter as? SceneContract.Presenter)?.onShowNotePicked(cue.cueId)
                }
            }

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

    override fun showContextMenu(context: CueInfo) {
        this.cueInfo = context
        recyclerView?.let { CueMenuHelper(context).openContextMenu(it) }
    }

    override fun showBookmarksDialog(bookmarks: List<Pair<Int, String>>) {
        val currentContext = context ?: return
        showBookmarksDialog(currentContext, bookmarks)
    }

    override fun showHasBookmarks(hasBookmarks: Boolean) {
        this.hasBookmarks = hasBookmarks
        activity?.invalidateOptionsMenu()
    }

    override fun showNote(note: String) {
        val currentActivity = activity ?: return
        AlertDialog.Builder(currentActivity)
                .setTitle(R.string.menu_note)
                .setIcon(R.drawable.ic_note)
                .setMessage(note)
                .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    override fun showNotePrompt(cueId: Int, title: String, note: String) {
        showInputPrompt(
                title = title,
                value = note,
                icon = R.drawable.ic_edit_note,
                onEdited = { onNoteEdited(cueId, it) }
        )
    }

    override fun showEditCuePrompt(cueId: Int, content: String, characters: List<CharacterInfo>, selected: CharacterInfo?) {
        showInputWithCharacterPrompt(
                title = getString(R.string.menu_editCue),
                value = content,
                icon = R.drawable.ic_edit_cue,
                characters = characters,
                selected = selected,
                onEdited = { t, c -> onCueEdited(cueId, t, c) }
        )
    }

    override fun showAddDialogPrompt(afterCueId: Int, characters: List<CharacterInfo>, selected: CharacterInfo?) {
        showInputWithCharacterPrompt(
                title = getString(R.string.menu_addAction),
                value = "",
                icon = R.drawable.ic_edit_cue,
                characters = characters,
                selected = selected,
                onEdited = { t, c -> onDialogWritten(afterCueId, t, c) }
        )
    }

    override fun showAddActionPrompt(afterCueId: Int, characters: List<CharacterInfo>, selected: CharacterInfo?) {
        showInputWithCharacterPrompt(
                title = getString(R.string.menu_addAction),
                value = "",
                icon = R.drawable.ic_edit_cue,
                characters = characters,
                selected = selected,
                onEdited = { t, c -> onActionWritten(afterCueId, t, c) }
        )
    }

    override fun showAddLyricsPrompt(afterCueId: Int, characters: List<CharacterInfo>, selected: CharacterInfo?) {
        showInputWithCharacterPrompt(
                title = getString(R.string.menu_addLyrics),
                value = "",
                icon = R.drawable.ic_edit_cue,
                characters = characters,
                selected = selected,
                onEdited = { t, c -> onLyricsWritten(afterCueId, t, c) }
        )
    }

    override fun showDeleteConfirm(cueId: Int, title: String) {
        val currentActivity = activity ?: return
        AlertDialog.Builder(currentActivity)
                .setIcon(R.drawable.ic_delete)
                .setTitle(title)
                .setMessage(R.string.scene_prompt_deleteWarningPrompt)
                .setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setNegativeButton(android.R.string.ok) { dialog, _ ->
                    (presenter as? SceneContract.Presenter)?.onDeleteCueConfirmed(cueId)
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    override fun showError(throwable: Throwable) {
        Snackbar.make(contentView, throwable.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    // endregion

    // region Internal

    private fun showBookmarksDialog(context: Context,
                                    bookmarks: List<Pair<Int, String>>) {
        val bookmarkValues = bookmarks.map { it.second }.toTypedArray()
        val builder = AlertDialog.Builder(context)
                .setTitle(R.string.scene_prompt_bookmark)
                .setIcon(R.drawable.ic_bookmarks_dark)
                .setItems(bookmarkValues) { d, w ->
                    val bookmarkId = bookmarks[w].first
                    (presenter as? SceneContract.Presenter)?.onBookmarkPicked(bookmarkId)
                    d.dismiss()
                }


        val dialog = builder.create()
        dialog.show()
    }

    private fun showInputPrompt(title: String,
                                @DrawableRes icon: Int,
                                value: String,
                                onEdited: SceneContract.Presenter.(String) -> Unit) {
        val currentActivity = activity ?: return

        val inputLayout = LayoutInflater.from(activity).inflate(R.layout.dialog_prompt, null, false) as TextInputLayout
        val inputText = inputLayout.findViewById<TextInputEditText>(R.id.input)
        inputText.setText(value)
        inputText.requestFocus()

        AlertDialog.Builder(currentActivity)
                .setTitle(title)
                .setIcon(icon)
                .setView(inputLayout)
                .setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    (presenter as? SceneContract.Presenter)?.onEdited(inputText.text.toString())
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    private fun showInputWithCharacterPrompt(title: String,
                                             @DrawableRes icon: Int,
                                             value: String,
                                             characters: List<CharacterInfo>,
                                             selected: CharacterInfo?,
                                             onEdited: SceneContract.Presenter.(String, CharacterInfo) -> Unit) {
        val currentActivity = activity ?: return

        val layout = LayoutInflater.from(activity).inflate(R.layout.dialog_prompt_with_character, null, false)

        val inputText = layout.findViewById<TextInputEditText>(R.id.input)
        inputText.setText(value)
        inputText.requestFocus()

        val characterSpinner = layout.findViewById<Spinner>(R.id.characters)
        val adapter = ArrayAdapter<CharacterInfo>(currentActivity, R.layout.spinner_item_character, characters)
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_character)
        characterSpinner.adapter = adapter
        val indexOf = characters.indexOf(selected)
        if (indexOf >= 0) characterSpinner.setSelection(indexOf)


        AlertDialog.Builder(currentActivity)
                .setTitle(title)
                .setIcon(icon)
                .setView(layout)
                .setNeutralButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(android.R.string.ok) { dialog, _ ->

                    (presenter as? SceneContract.Presenter)?.onEdited(inputText.text.toString(), characterSpinner.selectedItem as CharacterInfo)
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    // endregion
}
