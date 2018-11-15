package fr.xgouchet.rehearsal.cast

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.takisoft.colorpicker.ColorPickerDialog
import com.takisoft.colorpicker.OnColorSelectedListener
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_VALUE_CHANGED
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment


class CastFragment
    : ItemListFragment(),
        CastContract.View,
        OnColorSelectedListener {

    private var colorPickerRequest: Int = 0

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

    // region CastContract.View

    override fun showColorPicker(requestId: Int, @ColorInt color: Int) {

        val currentActivity = activity ?: return

        colorPickerRequest = requestId
        val params = ColorPickerDialog.Params.Builder(currentActivity.applicationContext)
                .setSelectedColor(color)
                .setColors(resources.getIntArray(R.array.character_color))
                .build()


        val dialog = ColorPickerDialog(currentActivity, this, params)
        dialog.show()
    }

    override fun showEnginePicker(requestId: Int, engine: String?) {
        val currentContext = context ?: return

        val servicePackageNames = listTTSServices(currentContext)

        if (servicePackageNames.size > 1) {
            showEngineDialog(requestId, currentContext, servicePackageNames)
        }
    }

    private fun showEngineDialog(requestId: Int,
                                 context: Context,
                                 servicePackageNames: Array<String>) {
        val builder = AlertDialog.Builder(context)
                .setTitle("Choose an voice provider")
                .setItems(servicePackageNames) { d, w ->
                    val engine = servicePackageNames[w]
                    (presenter as? CastContract.Presenter)?.onEnginePicked(requestId, engine)
                    d.dismiss()
                }

        val dialog = builder.create()
        dialog.show()
    }

    private fun listTTSServices(currentContext: Context): Array<String> {
        val pm = currentContext.packageManager
        val voiceIntent = Intent(TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE)
        val services = pm.queryIntentServices(voiceIntent, PackageManager.GET_META_DATA)

        return services.map { it.serviceInfo.packageName }
                .toTypedArray()
    }

    override fun showError(throwable: Throwable) {
        Snackbar.make(contentView, throwable.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    // endregion

    // region OnColorSelectedListener

    override fun onColorSelected(@ColorInt color: Int) {
        (presenter as? CastContract.Presenter)?.onColorPicked(colorPickerRequest, color)
    }

    // endregion
}
