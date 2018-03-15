package com.sedsoftware.yaptalker.presentation.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import com.sedsoftware.yaptalker.R
import com.squareup.picasso.Transformation

@Suppress("VariableMinLength")
class ImageOverlayTransformation(private val context: Context) : Transformation {

  @Suppress("UnsafeCallOnNullableType")
  companion object {
    private fun drawableRefToBitmap(context: Context, redId: Int): Bitmap {

      val outValue = TypedValue()
      context.theme.resolveAttribute(R.attr.themeName, outValue, true)
      val currentThemeName = outValue.string

      val currentThemeId = when (currentThemeName) {
        context.getString(R.string.theme_dark) -> R.style.AppTheme_Dark
        else -> R.style.AppTheme
      }

      val attr = context.theme.obtainStyledAttributes(currentThemeId, intArrayOf(redId))
      val attributeResourceId = attr.getResourceId(0, 0)
      val drawable = ResourcesCompat.getDrawable(context.resources, attributeResourceId, null)
      attr.recycle()

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
    val overlayBitmap = drawableRefToBitmap(context, R.attr.iconVideoOverlay)

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

  override fun key(): String = "overlay(x=$x, y=$y)"
}
