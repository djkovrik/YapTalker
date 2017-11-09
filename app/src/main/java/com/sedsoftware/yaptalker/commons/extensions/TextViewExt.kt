package com.sedsoftware.yaptalker.commons.extensions

import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.PicassoImageGetter
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

var TextView.textColor: Int
  get() = currentTextColor
  set(v) = setTextColor(context.color(v))

@Suppress("DEPRECATION")
fun TextView.textFromHtml(html: String) {

  Single
      .just(html)
      .map { text ->
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
          Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
          Html.fromHtml(text)
        }
      }
      .subscribeOn(Schedulers.computation())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(getSpannedObservable(this))
}

@Suppress("DEPRECATION")
fun TextView.textFromHtmlWithEmoji(html: String) {
  this.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, PicassoImageGetter(context, this), null)
  } else {
    Html.fromHtml(html, PicassoImageGetter(context, this), null)
  }
}

@Suppress("MagicNumber")
fun TextView.loadRatingBackground(rating: Int) {

  when (rating) {
  // Platinum
    in 1000..50000 -> {
      textColor = R.color.colorRatingPlatinumText
      setBackgroundResource(R.drawable.topic_rating_platinum)
    }
  // Gold
    in 500..999 -> {
      textColor = R.color.colorRatingGoldText
      setBackgroundResource(R.drawable.topic_rating_gold)
    }
  // Green
    in 50..499 -> {
      textColor = R.color.colorRatingGreenText
      setBackgroundResource(R.drawable.topic_rating_green)
    }
  // Gray
    in -9..49 -> {
      textColor = R.color.colorRatingGreyText
      setBackgroundResource(R.drawable.topic_rating_grey)
    }
  // Red
    in -99..-10 -> {
      textColor = R.color.colorRatingRedText
      setBackgroundResource(R.drawable.topic_rating_red)
    }
  // Dark Red
    else -> {
      textColor = R.color.colorRatingDarkRedText
      setBackgroundResource(R.drawable.topic_rating_dark_red)
    }
  }
}

private fun getSpannedObservable(textView: TextView) =
    object : SingleObserver<Spanned> {
      override fun onSubscribe(d: Disposable) {

      }

      override fun onSuccess(spanned: Spanned) {
        textView.text = spanned
      }

      override fun onError(e: Throwable) {
        Timber.d("Can't set spanned text to $textView: ${e.message}")
      }
    }
