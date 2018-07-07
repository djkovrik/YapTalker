package com.sedsoftware.yaptalker.presentation.custom

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import com.squareup.picasso.Transformation

/**
 * Circle image transformation for Picasso
 * Source: https://gist.github.com/julianshen/5829333
 */
@Suppress("VariableMinLength")
class CircleImageTransformation : Transformation {

    private var x: Int = 0
    private var y: Int = 0

    override fun transform(source: Bitmap): Bitmap {

        val size = Math.min(source.width, source.height)

        x = (source.width - size) / 2
        y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)

        if (squaredBitmap != source) {
            source.recycle()
        }

        val config = source.config ?: Bitmap.Config.ARGB_8888
        val bitmap = Bitmap.createBitmap(size, size, config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val bitmapShader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        paint.apply {
            shader = bitmapShader
            isAntiAlias = true
        }

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String = "rounded(x=$x, y=$y)"
}
