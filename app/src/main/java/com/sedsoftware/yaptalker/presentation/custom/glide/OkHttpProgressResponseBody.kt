package com.sedsoftware.yaptalker.presentation.custom.glide

import com.sedsoftware.yaptalker.presentation.extensions.orZero
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source
import java.io.IOException

class OkHttpProgressResponseBody(
    private val url: HttpUrl,
    private val responseBody: ResponseBody?,
    private val progressListener: ResponseProgressListener?
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long =
        responseBody?.contentLength().orZero()

    override fun contentType(): MediaType? =
        responseBody?.contentType()

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody?.source()))
        }

        return bufferedSource
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun source(source: Source?): Source =
        object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                val fullLength = responseBody?.contentLength().orZero()
                if (bytesRead == -1L) {
                    totalBytesRead = fullLength
                } else {
                    totalBytesRead += bytesRead
                }
                progressListener?.update(url, totalBytesRead, fullLength)
                return bytesRead
            }
    }
}
