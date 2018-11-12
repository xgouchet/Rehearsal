package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.StringRes
import fr.xgouchet.rehearsal.R

class ItemSlider {

    // region VM

    data class ViewModel(
            val id: Long,
            val label: String = "",
            @StringRes val labelRes: Int = 0,
            val value: Float = 0f,
            val min: Float = 0f,
            val max: Float = 100f,
            val steps: Int = 100,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.SLIDER

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data
    }

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            private val listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView),
            SeekBar.OnSeekBarChangeListener {

        private val labelView: TextView = itemView.findViewById(R.id.label)
        private val seekView: SeekBar = itemView.findViewById(R.id.slider)
        private val hintView: TextView = itemView.findViewById(R.id.hint)

        init {
            seekView.max = 100
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            if (item.label.isNotBlank()) {
                labelView.text = item.label
            } else if (item.labelRes != 0) {
                labelView.setText(item.labelRes)
            }

            seekView.setOnSeekBarChangeListener(null)

            seekView.max = item.steps
            val progress = (item.value - item.min) / (item.max - item.min)
            seekView.progress = (progress * item.steps).toInt()
            updateHint(item.value)

            seekView.setOnSeekBarChangeListener(this)
        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val progressFloat = computeProgress()
            updateHint(progressFloat)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            val progressFloat = computeProgress()
            updateHint(progressFloat)
            listener?.invoke(boundItem, ACTION_VALUE_CHANGED, progressFloat.toString())
        }

        private fun updateHint(progressFloat: Float) {
            hintView.text = String.format("%.1f", progressFloat)
        }

        private fun computeProgress(): Float {
            val progress = seekView.progress / boundItem.steps.toFloat()
            return (boundItem.min + (progress * (boundItem.max - boundItem.min)))
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_slider, parent, false)
            return ViewHolder(view, listener)
        }

    }
}
