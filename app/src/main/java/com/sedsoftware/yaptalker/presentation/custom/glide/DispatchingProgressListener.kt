package com.sedsoftware.yaptalker.presentation.custom.glide

import android.os.Handler
import android.os.Looper
import okhttp3.HttpUrl
import java.util.WeakHashMap

class DispatchingProgressListener : ResponseProgressListener {

    private val handler: Handler = Handler(Looper.getMainLooper())

    companion object {
        private val listeners = WeakHashMap<String, UiOnProgressListener>()
        private val progresses = WeakHashMap<String, Long>()

        private const val MAX_PROGRESS_VALUE = 100f

        fun forget(url: String) {
            listeners.remove(url)
            progresses.remove(url)
        }

        fun expect(url: String, listener: UiOnProgressListener) {
            listeners[url] = listener
        }
    }

    override fun update(url: HttpUrl, bytesRead: Long, contentLength: Long) {
        val key = url.toString()
        val listener = listeners[key] ?: return

        if (contentLength <= bytesRead) {
            forget(key)
        }

        if (needsDispatch(key, bytesRead, contentLength, listener.getGranualityPercentage())) {
            handler.post { listener.onProgress(bytesRead, contentLength) }
        }
    }

    private fun needsDispatch(key: String, current: Long, total: Long, granularity: Float): Boolean {
        if (granularity == 0f || current == 0L || total == current) {
            return true
        }
        val percent = MAX_PROGRESS_VALUE * current / total
        val currentProgress = (percent / granularity).toLong()
        val lastProgress = progresses[key]
        return if (lastProgress == null || currentProgress != lastProgress) {
            progresses[key] = currentProgress
            true
        } else {
            false
        }
    }
}
