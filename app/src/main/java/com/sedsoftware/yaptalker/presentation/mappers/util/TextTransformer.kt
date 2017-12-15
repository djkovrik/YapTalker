package com.sedsoftware.yaptalker.presentation.mappers.util

import android.content.Context
import android.text.Html
import android.text.Spanned
import javax.inject.Inject

class TextTransformer @Inject constructor(private val context: Context) {

  @Suppress("DEPRECATION")
  fun transformHtmlToSpanned(html: String): Spanned =
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
      } else {
        Html.fromHtml(html)
      }
}