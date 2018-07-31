package com.sedsoftware.yaptalker.presentation.custom.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class GlideWithProgressImageLoader(
    private val imageView: ImageView,
    private val progressBar: ProgressBar?,
    private val textLabel: TextView?
) {

    fun load(url: String, options: RequestOptions) {

        onConnecting()

        DispatchingProgressListener.expect(url, object : UiOnProgressListener {
            override fun onProgress(bytesRead: Long, expectedLength: Long) {
                progressBar?.progress = (100 * bytesRead / expectedLength).toInt()
            }

            override fun getGranualityPercentage(): Float = 1.0f
        })

        GlideApp.with(imageView.context)
            .load(url)
            .apply(options)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    DispatchingProgressListener.forget(url)
                    onFinished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    DispatchingProgressListener.forget(url)
                    onFinished()
                    return false
                }
            })
            .into(imageView)

    }


    private fun onConnecting() {
        textLabel?.isVisible = true
        progressBar?.isVisible = true
        imageView.isGone = true
    }

    private fun onFinished() {
        textLabel?.isGone = true
        progressBar?.isGone = true
        imageView.isVisible = true
    }
}
