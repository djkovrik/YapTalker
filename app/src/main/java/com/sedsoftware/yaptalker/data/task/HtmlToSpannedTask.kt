package com.sedsoftware.yaptalker.data.task

import android.text.Html
import android.text.Spanned
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Suppress("DEPRECATION")
class HtmlToSpannedTask @Inject constructor() : Task<Spanned, HtmlToSpannedTask.Params>(Schedulers.computation()) {

  override fun buildTaskSingle(params: Params): Single<Spanned> =

      object : Single<Spanned>() {

        override fun subscribeActual(observer: SingleObserver<in Spanned>) {

          val spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(params.html, Html.FROM_HTML_MODE_LEGACY)
          } else {
            Html.fromHtml(params.html)
          }

          observer.onSuccess(spanned)
        }
      }

  class Params(val html: String)
}
