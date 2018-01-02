package com.sedsoftware.yaptalker.presentation.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import com.sedsoftware.yaptalker.R
import com.squareup.picasso.Transformation


@Suppress("VariableMinLength")
class ImageOverlayTransformation(private val context: Context) : Transformation {

  companion object {
    fun drawableToBitmap(context: Context, @DrawableRes drawableResId: Int): Bitmap {

      val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)

      if (drawable is BitmapDrawable) {
        return drawable.bitmap
      }

      var width = drawable!!.intrinsicWidth
      width = if (width > 0) width else 1
      var height = drawable.intrinsicHeight
      height = if (height > 0) height else 1

      val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      drawable.setBounds(0, 0, canvas.width, canvas.height)
      drawable.draw(canvas)

      return bitmap
    }
  }

  private var x: Float = 0f
  private var y: Float = 0f

  override fun transform(source: Bitmap): Bitmap {

    val config = source.config ?: Bitmap.Config.ARGB_8888
    val drawingBitmap = source.copy(config, true)
    val overlayBitmap = drawableToBitmap(context, R.drawable.ic_video_play)

    x = source.width / 2f - overlayBitmap.width / 2f
    y = source.height / 2f - overlayBitmap.height / 2f

    val canvas = Canvas(drawingBitmap)
    val paint = Paint()
    canvas.drawBitmap(overlayBitmap, x, y, paint)

    if (source != drawingBitmap) {
      source.recycle()
    }

    overlayBitmap.recycle()

    return drawingBitmap
  }

  override fun key(): String = "overlayed(x=$x, y=$y)"
}
