package com.sedsoftware.yaptalker.presentation.extensions

import android.text.Html
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.custom.PicassoImageGetter

var TextView.textColor: Int
    get() = currentTextColor
    set(v) = setTextColor(context.colorFromAttr(v))

@Suppress("MagicNumber")
fun TextView.loadRatingBackground(rating: Int) {

    when (rating) {
    // Platinum
        in 1000..50000 -> {
            textColor = R.attr.colorRatingPlatinumText
            setBackgroundResource(R.drawable.topic_rating_platinum)
        }
    // Gold
        in 500..999 -> {
            textColor = R.attr.colorRatingGoldText
            setBackgroundResource(R.drawable.topic_rating_gold)
        }
    // Green
        in 50..499 -> {
            textColor = R.attr.colorRatingGreenText
            setBackgroundResource(R.drawable.topic_rating_green)
        }
    // Gray
        in -9..49 -> {
            textColor = R.attr.colorRatingGreyText
            setBackgroundResource(R.drawable.topic_rating_grey)
        }
    // Red
        in -99..-10 -> {
            textColor = R.attr.colorRatingRedText
            setBackgroundResource(R.drawable.topic_rating_red)
        }
    // Dark Red
        else -> {
            textColor = R.attr.colorRatingDarkRedText
            setBackgroundResource(R.drawable.topic_rating_dark_red)
        }
    }
}

@Suppress("DEPRECATION")
fun TextView.textFromHtmlWithEmoji(html: String) {
    this.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, PicassoImageGetter(context, this), null)
    } else {
        Html.fromHtml(html, PicassoImageGetter(context, this), null)
    }
}
