package fr.xgouchet.rehearsal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import fr.xgouchet.rehearsal.R

class ItemRehearsal {

    // region VM

    class ViewModel(
            val id: Long,
            val title: String = "",
            val time: Time = Time.FUTURE,
            val data: Any? = null
    ) : Item.ViewModel() {
        override fun getItemType() = Item.Type.SCHEDULE

        override fun getItemStableId() = id

        override fun getItemData(): Any? = data

        enum class Time {
            PAST,
            PRESENT,
            FUTURE
        }
    }

    // endregion

    // region VH

    class ViewHolder(
            itemView: View,
            listener: ItemListener
    ) : Item.ViewHolder<ViewModel>(itemView) {

        private val iconView: ImageView = itemView.findViewById(R.id.icon)
        private val titleView: TextView = itemView.findViewById(R.id.title)

        init {
            itemView.setOnClickListener { listener(boundItem, ACTION_DEFAULT, null) }
        }

        @SuppressLint("SetTextI18n")
        override fun onBind(item: ViewModel) {

            titleView.text = item.title

            when (item.time) {
                ViewModel.Time.PAST -> {
                    titleView.setTextColor(ContextCompat.getColor(titleView.context, R.color.secondary_text))
                    iconView.setImageResource(R.drawable.ic_event_past)
                }
                ViewModel.Time.FUTURE -> {
                    titleView.setTextColor(ContextCompat.getColor(titleView.context, R.color.primary_text))
                    iconView.setImageResource(R.drawable.ic_event_future)
                }
                ViewModel.Time.PRESENT -> {
                    titleView.setTextColor(ContextCompat.getColor(titleView.context, R.color.accent))
                    iconView.setImageResource(R.drawable.ic_event_today)
                }
            }
        }

    }

    // endregion

    companion object {
        @JvmStatic
        fun instantiateViewHolder(inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  listener: ItemListener)
                : ViewHolder {
            val view = inflater.inflate(R.layout.item_schedule, parent, false)
            return ViewHolder(view, listener)
        }
    }
}
