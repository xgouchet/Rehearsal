package fr.xgouchet.rehearsal.ui

import android.text.format.DateFormat
import fr.xgouchet.rehearsal.ext.year
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormatter {


    private val reference = Calendar.getInstance()
    private val mutable = Calendar.getInstance()

    private val dateNoYear: SimpleDateFormat
    private val dateWithYear: SimpleDateFormat

    init {
        // val hourFormat = if (DateFormat.is24HourFormat(context)) "HH" else "KK"

        dateNoYear = bestFormat("EE dd MMM")
        dateWithYear = bestFormat("EE dd MMM YYYY")

    }

    fun formatDate(date: Date): String {
        synchronized(mutable) {
            reference.timeInMillis = System.currentTimeMillis()
            mutable.time = date

            return if (reference.year() == mutable.year()) {
                dateNoYear.format(date)
            } else {
                dateWithYear.format(date)
            }
        }
    }

}

fun bestFormat(skeleton: String, locale: Locale = Locale.getDefault()): SimpleDateFormat {
    return SimpleDateFormat(
            DateFormat.getBestDateTimePattern(locale, skeleton),
            locale
    )
}

