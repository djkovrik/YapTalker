package com.sedsoftware.yaptalker.device.storage

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.sedsoftware.yaptalker.domain.device.UpdatesDownloader
import javax.inject.Inject

class YapUpdatesDownloader @Inject constructor(
  private val context: Context
) : UpdatesDownloader {

  override fun initiateUpdateDownloadSession(url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substringAfterLast("/"))
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
  }
}
