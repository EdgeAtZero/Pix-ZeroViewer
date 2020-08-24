package com.edgeatzero.projects.pixiv.model.util

import com.edgeatzero.library.ext.*
import com.edgeatzero.projects.pixiv.util.Settings
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object DataFormatUtils {

    fun formatLongNumber(number: Long) = when {
        number in 1000..1000000 -> "${String.format("%.2f", number / 1000F)}k"
        number >= 1000000 -> "${String.format("%.2f", number / 10000000F)}m"
        else -> "$number"
    }

    private val regex by lazy { "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}".toRegex() }
    private val pattern by lazy { "yyyy-MM-dd hh:mm:ssZ" }
    private val chars by lazy { arrayOf('T', ' ', ':') }

    val rankingStartTimeCalendar: Calendar
        get() = Calendar.getInstance(Settings.locale).apply {
            set(2007, 9, 13, 0, 0, 0)
        }

    val yesterdayTimeCalendar: Calendar
        get() = Calendar.getInstance(Settings.locale).apply {
            add(Calendar.DATE, -1)
            hourOfDay = 23
            minute = 59
            second = 59
        }

    fun formatPixivDate(
        string: String
    ): String = parseDate(string)?.let { autoFormatDate(it) } ?: string

    fun autoFormatDate(date: Calendar): String {
        val now = Calendar.getInstance(Settings.locale)
        val pattern = when {
            date.year < now.year -> "yyyy-M-d"
            date.year == now.year && date.month == now.month && date.dayOfMonth == now.dayOfMonth -> "HH:mm:ss"
            else -> "M-d"
        }
        return SimpleDateFormat(pattern, Settings.locale).format(date.timeInMillis)
    }

    fun parseDate(date: String): Calendar? {
        val position = ParsePosition(0)
        val text = if (date.matches(regex)) formatDate(date) else date
        val parse = SimpleDateFormat(pattern, Settings.locale).parse(text, position) ?: return null
        if (position.index == 1) return null
        val instance = Calendar.getInstance(Settings.locale)
        instance.timeInMillis = parse.time
        return instance
    }

    fun formatDate(date: String): String {
        val s = date.replace(chars[0], chars[1], false)
        val indexOf = s.lastIndexOf(chars[2])
        return s.removeRange(indexOf, indexOf + 1)
    }

}
