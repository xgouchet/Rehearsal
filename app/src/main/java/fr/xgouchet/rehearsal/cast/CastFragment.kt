package fr.xgouchet.rehearsal.cast

import androidx.annotation.ColorInt
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

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?) {
        when (action) {
            ACTION_DEFAULT -> (presenter as? CastContract.Presenter)?.onItemSelected(item)
            ACTION_VALUE_CHANGED -> (presenter as? CastContract.Presenter)?.onItemValueChanged(item, value)
        }

    }

    // endregion

    // region CastContract.View

    override fun showColorPicker(requestId: Int, @ColorInt selectedColor: Int) {

        val currentActivity = activity ?: return

        colorPickerRequest = requestId
        val params = ColorPickerDialog.Params.Builder(currentActivity.applicationContext)
                .setSelectedColor(selectedColor)
                .setColors(resources.getIntArray(R.array.character_color))
                .build()


        val dialog = ColorPickerDialog(currentActivity, this, params)
        dialog.show()
    }

    // endregion

    // region OnColorSelectedListener

    override fun onColorSelected(@ColorInt color: Int) {
        (presenter as? CastContract.Presenter)?.onColorPicked(colorPickerRequest, color)
    }

    // endregion
}
