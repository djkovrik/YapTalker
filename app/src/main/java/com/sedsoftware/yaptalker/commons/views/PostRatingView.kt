package com.sedsoftware.yaptalker.commons.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.textColor
import java.util.Locale

class PostRatingView : TextView {

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

  var ratingText: CharSequence
    get() {
      return text
    }
    set(value) {
      setPostRank(value.toString())
    }

  private fun setPostRank(postRank: String) {

    if (postRank.isNotEmpty()) {
      val rank = postRank.toInt()

      when {
        rank > 0 -> {
          text = String.format(Locale.getDefault(), "+%d", rank)
          textColor = R.color.colorRatingGreen
        }
        rank < 0 -> {
          text = String.format(Locale.getDefault(), "%d", rank)
          textColor = R.color.colorRatingRed
        }
        else -> {
          text = postRank
          textColor = R.color.colorSupportingText
        }
      }
    }
  }
}
