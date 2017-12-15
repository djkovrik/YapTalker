package com.sedsoftware.yaptalker.presentation.mappers.util

import android.content.Context
import android.text.Html
import android.text.Spanned
import java.util.Locale
import javax.inject.Inject

class TextTransformer @Inject constructor(private val context: Context) {

  companion object {
    private const val POSITIVE_RATING = "<font color='#4CAF50'>+%d</font>"
    private const val NEGATIVE_RATING = "<font color='#E57373'>-%d</font>"
    private const val ZERO_RATING = "<font color='#BDBDBD'>%d</font>"
  }

  @Suppress("DEPRECATION")
  fun transformHtmlToSpanned(html: String): Spanned =
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
      } else {
        Html.fromHtml(html)
      }

  @Suppress("DEPRECATION")
  fun transformRankToFormattedText(rank: Int): Spanned {
    val html = when {
      rank > 0 -> {
        String.format(Locale.getDefault(), POSITIVE_RATING, rank)
      }
      rank < 0 -> {
        String.format(Locale.getDefault(), NEGATIVE_RATING, rank)
      }
      else -> {
        String.format(Locale.getDefault(), ZERO_RATING, rank)
      }
    }

    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
      Html.fromHtml(html)
    }
  }
}
