package fr.xgouchet.rehearsal.screen.props

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import com.takisoft.colorpicker.ColorPickerDialog
import com.takisoft.colorpicker.OnColorSelectedListener
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.screen.cast.CastContract
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_VALUE_CHANGED
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class PropsFragment
    : ItemListFragment(),
        PropsContract.View{

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        var consumed = true
        when (action) {
            ACTION_DEFAULT -> (presenter as? CastContract.Presenter)?.onItemSelected(item)
            ACTION_VALUE_CHANGED -> (presenter as? CastContract.Presenter)?.onItemValueChanged(item, value)
            else -> consumed = false
        }
        return consumed
    }

    // endregion

    // region PropsContract.View

    override fun showError(throwable: Throwable) {
        showSnackbarError(throwable)
    }

    // endregion

}
