package com.sedsoftware.yaptalker.presentation.custom

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadManagerReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action ||
      DownloadManager.ACTION_NOTIFICATION_CLICKED == intent.action) {
      context.startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
    }
  }
}
