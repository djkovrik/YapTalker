package com.sedsoftware.yaptalker.commons.views

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.stringQuantityRes
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ShortDateView : AppCompatTextView {

  companion object {
    private const val MILLISECONDS_PER_SECOND = 1000
    private const val SECONDS_PER_MINUTE = 60
    private const val MINUTES_PER_HOUR = 60
    private const val HOURS_PER_DAY = 24
    private const val DAYS_PER_MONTH = 30
    private const val MONTH_PER_YEAR = 12
  }

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

  var shortDateText: CharSequence
    get() = text
    set(value) {
      Single
          .just(value)
          .observeOn(Schedulers.computation())
          .map { text -> getDifference(text) }
          .map { diff -> getCalculatedTime(diff) }
          .map { calculatedTime -> buildString(calculatedTime) }
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(getTextObservable(this))
    }

  private fun getDifference(source: CharSequence): Int {

    val format = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
    val sourceStr = source.toString()
    val topicDate = format.parse(sourceStr)
    val currentDate = Calendar.getInstance().time

    return ((currentDate.time - topicDate.time) / MILLISECONDS_PER_SECOND).toInt()

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

  private fun buildString(time: CalculatedTime): String {
    return when {
      time.years > 0 -> {
        val template = context.stringQuantityRes(R.plurals.short_date_years, time.years)
        String.format(Locale.getDefault(), template, time.years)
      }
      time.months > 0 -> {
        val template = context.stringRes(R.string.short_date_month)
        String.format(Locale.getDefault(), template, time.months)
      }
      time.days > 0 -> {
        val template = context.stringQuantityRes(R.plurals.short_date_days, time.days)
        String.format(Locale.getDefault(), template, time.days)
      }
      time.hours > 0 -> {
        val template = context.stringQuantityRes(R.plurals.short_date_hours, time.hours)
        String.format(Locale.getDefault(), template, time.hours)
      }
      time.minutes > 0 -> {
        val template = context.stringRes(R.string.short_date_minutes)
        String.format(Locale.getDefault(), template, time.minutes)
      }
      else -> context.stringRes(R.string.short_date_seconds_now)
    }
  }

  private fun getTextObservable(shortDateView: ShortDateView) =
      object : SingleObserver<String> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onSuccess(str: String) {
          shortDateView.text = str
        }

        override fun onError(e: Throwable) {
          Timber.d("Can't set date text to $shortDateView: ${e.message}")
          shortDateView.text = ""
        }
      }

  private inner class CalculatedTime(
      val minutes: Int,
      val hours: Int,
      val days: Int,
      val months: Int,
      val years: Int)
}
