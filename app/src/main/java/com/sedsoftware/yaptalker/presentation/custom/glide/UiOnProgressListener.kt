package com.sedsoftware.yaptalker.presentation.custom.glide

interface UiOnProgressListener {
    fun onProgress(bytesRead: Long, expectedLength: Long)
    fun getGranualityPercentage(): Float
}
