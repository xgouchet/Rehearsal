package fr.xgouchet.rehearsal.ui

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import fr.xgouchet.rehearsal.R

object CharacterColor {

    @ColorInt
    fun get(context: Context, colorIndex: Int): Int {
        val colorIdx = colorIndex % characterColors.size
        val colorRes = if (colorIndex >= 0) characterColors[colorIdx] else unknown
        return ContextCompat.getColor(context, colorRes)
    }


    private const val unknown = R.color.character_fg_unknown

    private val characterColors = listOf(
            R.color.character_fg_0,
            R.color.character_fg_1,
            R.color.character_fg_2,
            R.color.character_fg_3,
            R.color.character_fg_4,
            R.color.character_fg_5,
            R.color.character_fg_6,
            R.color.character_fg_7,
            R.color.character_fg_8,
            R.color.character_fg_9,
            R.color.character_fg_10,
            R.color.character_fg_11,
            R.color.character_fg_12,
            R.color.character_fg_13,
            R.color.character_fg_14,
            R.color.character_fg_15
    )
}
