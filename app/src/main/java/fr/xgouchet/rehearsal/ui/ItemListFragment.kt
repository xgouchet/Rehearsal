package fr.xgouchet.rehearsal.ui

import android.app.AlertDialog
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.archx.list.ArchXListFragment

abstract class ItemListFragment
    : ArchXListFragment<Item.ViewModel, ItemAdapter>() {

    // region ArchXListFragment

    override fun createAdapter(): ItemAdapter {
        return ItemAdapter { o, a, v ->
            onItemAction(o, a, v)
        }
    }

    override fun showData(viewModel: List<Item.ViewModel>) {
        adapter.updateData(viewModel)
    }

    // endregion


    // region UI Utils

    fun showSnackbarError(throwable: Throwable) {
        Snackbar.make(contentView, throwable.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    fun confirmDelete(@StringRes title: Int,
                      @StringRes message: Int,
                      onDeleteConfirmed: () -> Unit) {
        val currentActivity = activity ?: return

        AlertDialog.Builder(currentActivity)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(android.R.string.cancel) { a, _ -> a.dismiss() }
                .setNegativeButton(android.R.string.ok) { _, _ ->
                    onDeleteConfirmed()
                }
                .create()
                .show()
    }

    // endregion

    protected abstract fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean

}
