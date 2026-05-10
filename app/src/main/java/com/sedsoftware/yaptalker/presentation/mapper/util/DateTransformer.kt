package com.sedsoftware.yaptalker.presentation.mapper.util

import android.content.Context
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.quantityString
import com.sedsoftware.yaptalker.presentation.extensions.string
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateTransformer @Inject constructor(private val context: Context) {

    companion object {
        private const val MILLISECONDS_PER_SECOND = 1000
        private const val SECONDS_PER_MINUTE = 60
        private const val MINUTES_PER_HOUR = 60
        private const val HOURS_PER_DAY = 24
        private const val DAYS_PER_MONTH = 30
        private const val MONTH_PER_YEAR = 12

        private val MONTHS_NORMALIZATION = mapOf(
            "\u044f\u043d\u0432" to "01",
            "\u0444\u0435\u0432" to "02",
            "\u043c\u0430\u0440" to "03",
            "\u0430\u043f\u0440" to "04",
            "\u043c\u0430\u044f" to "05",
            "\u043c\u0430\u0439" to "05",
            "\u0438\u044e\u043d" to "06",
            "\u0438\u044e\u043b" to "07",
            "\u0430\u0432\u0433" to "08",
            "\u0441\u0435\u043d" to "09",
            "\u0441\u0435\u043d\u0442" to "09",
            "\u043e\u043a\u0442" to "10",
            "\u043d\u043e\u044f" to "11",
            "\u0434\u0435\u043a" to "12"
        )
        private val MONTH_NAMES = listOf(
            "\u044f\u043d\u0432",
            "\u0444\u0435\u0432",
            "\u043c\u0430\u0440",
            "\u0430\u043f\u0440",
            "\u043c\u0430\u044f",
            "\u0438\u044e\u043d",
            "\u0438\u044e\u043b",
            "\u0430\u0432\u0433",
            "\u0441\u0435\u043d",
            "\u043e\u043a\u0442",
            "\u043d\u043e\u044f",
            "\u0434\u0435\u043a"
        )
    }

    fun transformDateToShortView(date: String): String {
        val diff = getDifference(date)
        val calcTime = getCalculatedTime(diff)
        return buildString(calcTime)
    }

    fun transformLongToDateString(value: Long): String {
        return when {
            value != 0L -> {
                val calendar = Calendar.getInstance().apply {
                    time = Date(value)
                }
                String.format(
                    Locale.getDefault(),
                    "%d %s %d \u0432 %02d:%02d",
                    calendar.get(Calendar.DAY_OF_MONTH),
                    MONTH_NAMES[calendar.get(Calendar.MONTH)],
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
                )
            }
            else -> ""
        }
    }

    private fun getDifference(source: String): Int {

        val normalizedSource = normalizeDateSource(source)
        val format = SimpleDateFormat("d MM yyyy '\u0432' HH:mm", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val topicDate = format.parse(normalizedSource) ?: currentDate

        return ((currentDate.time - topicDate.time) / MILLISECONDS_PER_SECOND).toInt()

    }

    private fun normalizeDateSource(source: String): String {
        val dateParts = source.trim().lowercase(Locale.getDefault()).split(Regex("\\s+")).toMutableList()
        if (dateParts.size > 1) {
            dateParts[1] = MONTHS_NORMALIZATION[dateParts[1]] ?: dateParts[1]
        }
        return dateParts.joinToString(" ")
    }

    private fun getCalculatedTime(diff: Int): CalculatedTime {
        var diffInSeconds = diff

        // Skip seconds
        diffInSeconds /= MINUTES_PER_HOUR
        val min = if (diffInSeconds >= SECONDS_PER_MINUTE) (diffInSeconds % SECONDS_PER_MINUTE) else diffInSeconds
        diffInSeconds /= SECONDS_PER_MINUTE
        val hrs = if (diffInSeconds >= HOURS_PER_DAY) (diffInSeconds % HOURS_PER_DAY) else diffInSeconds
        diffInSeconds /= HOURS_PER_DAY
        val days = if (diffInSeconds >= DAYS_PER_MONTH) (diffInSeconds % DAYS_PER_MONTH) else diffInSeconds
        diffInSeconds /= DAYS_PER_MONTH
        val months = if (diffInSeconds >= MONTH_PER_YEAR) (diffInSeconds % MONTH_PER_YEAR) else diffInSeconds
        diffInSeconds /= MONTH_PER_YEAR
        val years = diffInSeconds

        return CalculatedTime(min, hrs, days, months, years)
    }

    private fun buildString(time: CalculatedTime): String =
        when {
            time.years > 0 -> {
                val template = context.quantityString(R.plurals.short_date_years, time.years)
                String.format(Locale.getDefault(), template, time.years)
            }
            time.months > 0 -> {
                val template = context.string(R.string.short_date_month)
                String.format(Locale.getDefault(), template, time.months)
            }
            time.days > 0 -> {
                val template = context.quantityString(R.plurals.short_date_days, time.days)
                String.format(Locale.getDefault(), template, time.days)
            }
            time.hours > 0 -> {
                val template = context.quantityString(R.plurals.short_date_hours, time.hours)
                String.format(Locale.getDefault(), template, time.hours)
            }
            time.minutes > 0 -> {
                val template = context.string(R.string.short_date_minutes)
                String.format(Locale.getDefault(), template, time.minutes)
            }
            else -> context.string(R.string.short_date_seconds_now)
        }

    private inner class CalculatedTime(
        val minutes: Int,
        val hours: Int,
        val days: Int,
        val months: Int,
        val years: Int
    )
}
