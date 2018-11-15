package fr.xgouchet.rehearsal.ext

import java.util.Calendar

fun Calendar.year(): Int {
    return this[Calendar.YEAR]
}

fun Calendar.month(): Int {
    return this[Calendar.MONTH]
}

fun Calendar.weekOfYear(): Int {
    return this[Calendar.WEEK_OF_YEAR]
}

fun Calendar.weekOfMonth(): Int {
    return this[Calendar.WEEK_OF_MONTH]
}

fun Calendar.dayOfYear(): Int {
    return this[Calendar.DAY_OF_YEAR]
}

fun Calendar.dayOfMonth(): Int {
    return this[Calendar.DAY_OF_MONTH]
}

fun Calendar.dayOfWeek(): Int {
    return this[Calendar.DAY_OF_WEEK]
}

fun Calendar.dayOfWeekInMonth(): Int {
    return this[Calendar.DAY_OF_WEEK_IN_MONTH]
}

fun Calendar.hour(): Int {
    return this[Calendar.HOUR]
}

fun Calendar.hourOfDay(): Int {
    return this[Calendar.HOUR_OF_DAY]
}

fun Calendar.minute(): Int {
    return this[Calendar.MINUTE]
}

fun Calendar.second(): Int {
    return this[Calendar.SECOND]
}

fun Calendar.millisecond(): Int {
    return this[Calendar.MILLISECOND]
}
