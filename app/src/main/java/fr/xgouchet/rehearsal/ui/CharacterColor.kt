package fr.xgouchet.rehearsal.ui

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.CharacterModel

object CharacterColor {


    private var unknown: Int = -1

    private var characterColors: IntArray = IntArray(0)

    fun init(context: Context) {
        characterColors = listOf(
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
                .asSequence()
                .map { ContextCompat.getColor(context, it) }
                .toList()
                .toIntArray()

        unknown = ContextCompat.getColor(context, R.color.character_fg_unknown)
    }


    @ColorInt
    fun get(character: CharacterModel?): Int {
        return if (character == null) {
            unknown
        } else if (character.color != -1) {
            character.color
        } else {
            get(character.characterId)
        }
    }


    @ColorInt
    fun getHighlight(character: CharacterModel?): Int {
        val baseColor = get(character)

        val r = (Color.red(baseColor) / 8) + 224
        val g = (Color.green(baseColor) / 8) + 224
        val b = (Color.blue(baseColor) / 8) + 224
        return Color.rgb(r, g, b)
    }

    @ColorInt
    private fun get(colorIndex: Int): Int {
        val colorIdx = colorIndex % characterColors.size
        return if (colorIndex >= 0) characterColors[colorIdx] else unknown
    }


}
