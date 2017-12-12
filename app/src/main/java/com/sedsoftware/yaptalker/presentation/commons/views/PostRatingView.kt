package com.sedsoftware.yaptalker.presentation.commons.views

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.commons.extensions.hideView
import com.sedsoftware.yaptalker.presentation.commons.extensions.showView
import com.sedsoftware.yaptalker.presentation.commons.extensions.textColor
import java.util.Locale

class PostRatingView : AppCompatTextView {

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

  var ratingText: CharSequence
    get() = text
    set(value) {
      setPostRank(value.toString())
    }

  private fun setPostRank(postRank: String) {

    hideView()

    if (postRank.isNotEmpty()) {
      val rank = postRank.toInt()

      when {
        rank > 0 -> {
          text = String.format(Locale.getDefault(), "+%d", rank)
          textColor = R.color.colorRatingGreen
          showView()
        }
        rank < 0 -> {
          text = String.format(Locale.getDefault(), "%d", rank)
          textColor = R.color.colorRatingRed
          showView()
        }
      }
    }
  }
}
