package com.sedsoftware.yaptalker.presentation.extensions

import android.text.Html
import android.widget.TextView
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.custom.PicassoImageGetter
import kotlin.math.roundToInt

var TextView.textColor: Int
    get() = currentTextColor
    set(v) = setTextColor(context.colorFromAttr(v))

private val iconicsTokenRegex = Regex("\\{[a-z0-9]{3}[-a-z0-9_]*\\}\\s*")

fun CharSequence.withoutIconicsTokens(): String =
    iconicsTokenRegex.replace(this, "")

fun TextView.setStartIcon(icon: IIcon) {
    val iconSize = textSize.roundToInt().coerceAtLeast(1)
    val drawable = IconicsDrawable(context)
        .icon(icon)
        .color(currentTextColor)
        .sizePx(iconSize)

    compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen.post_karma_thump_icon_padding)
    setCompoundDrawablesRelative(drawable, null, null, null)
}

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
