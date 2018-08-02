package com.sedsoftware.yaptalker.presentation.custom.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.util.Locale

class GlideWithProgressImageLoader {

    companion object {
        private const val MAX_PROGRESS_VALUE = 100

        fun load(url: String,
                 imageView: ImageView,
                 progressBar: ProgressBar?,
                 textLabel: TextView?,
                 options: RequestOptions) {

            textLabel?.isVisible = true
            progressBar?.isVisible = true

            DispatchingProgressListener.expect(url, object : UiOnProgressListener {
                override fun onProgress(bytesRead: Long, expectedLength: Long) {
                    val progressValue = (MAX_PROGRESS_VALUE * bytesRead / expectedLength).toInt()
                    progressBar?.progress = progressValue
                    textLabel?.text = String.format(Locale.getDefault(), "%d %%", progressValue)
                }

                override fun getGranualityPercentage(): Float = 1.0f
            })

            Glide.with(imageView.context)
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
                        textLabel?.isGone = true
                        progressBar?.isGone = true
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
                        textLabel?.isGone = true
                        progressBar?.isGone = true
                        imageView.isVisible = true
                        return false
                    }
                })
                .into(imageView)
        }
    }
}
