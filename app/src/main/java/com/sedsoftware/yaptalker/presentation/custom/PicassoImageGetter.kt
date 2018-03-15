package com.sedsoftware.yaptalker.presentation.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.sedsoftware.yaptalker.presentation.extensions.currentDensity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target as DrawableTarget

class PicassoImageGetter(val context: Context, val textView: TextView) : Html.ImageGetter {

  override fun getDrawable(src: String?): Drawable {
    val drawable = EmojiDrawable()
    Picasso.with(context).load("http:$src").into(drawable)
    return drawable
  }

  inner class EmojiDrawable : DrawableTarget, BitmapDrawable(context.resources, "") {

    var drawable: Drawable? = null

    override fun draw(canvas: Canvas) {
      drawable?.draw(canvas)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
      setTargetDrawable(BitmapDrawable(context.resources, bitmap))
    }

    private fun setTargetDrawable(drawable: Drawable) {
      this.drawable = drawable
      val width = drawable.intrinsicWidth * context.currentDensity
      val height = drawable.intrinsicHeight * context.currentDensity
      drawable.setBounds(0, 0, width, height)
      setBounds(0, 0, width, height)
      textView.text = textView.text
    }
  }
}
