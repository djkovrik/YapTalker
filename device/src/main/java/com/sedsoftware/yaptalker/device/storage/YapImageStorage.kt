package com.sedsoftware.yaptalker.device.storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.sedsoftware.yaptalker.domain.device.ImageStorage
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class YapImageStorage @Inject constructor(
    private val context: Context,
    @Named("fileClient") private val httpClient: OkHttpClient
) : ImageStorage {

    override fun saveImage(url: String): Single<String> =
        loadImageFromUrl(url)
            .flatMap { response -> saveToDisk(response, url.substringAfterLast("/")) }
            .map { file -> file.absolutePath }

    private fun loadImageFromUrl(url: String): Single<Response> =
        Single.create<Response> { emitter ->
            try {
                val request = Request.Builder().url(url).build()
                val response = httpClient.newCall(request).execute()
                emitter.onSuccess(response)
            } catch (e: IOException) {
                emitter.onError(e)
            }
        }

    private fun saveToDisk(response: Response, filename: String): Single<File> =
        Single.create { emitter ->
            try {
                val storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )

                if (!storageDir.exists() && !storageDir.mkdir()) {
                    throw IOException("Can't create file path")
                }

                val file = File(storageDir, filename)
                val sink = Okio.buffer(Okio.sink(file))

                response.body()?.source()?.let { sink.writeAll(it) }
                sink.close()
                scanForMedia(file)
                emitter.onSuccess(file)

            } catch (e: IOException) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }

    private fun scanForMedia(image: File) {
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)))
    }
}
