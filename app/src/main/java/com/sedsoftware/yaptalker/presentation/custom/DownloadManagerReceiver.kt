package com.sedsoftware.yaptalker.presentation.custom

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File

class DownloadManagerReceiver : BroadcastReceiver() {

    companion object {
        private const val APK_MIME_TYPE = "application/vnd.android.package-archive"
        private const val APK_FILE_PROVIDER_AUTHORITY = "com.sedsoftware.yaptalker.device.fileprovider"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L)
            openDownloadedAttachment(context, downloadId)
        }
    }

    private fun openDownloadedAttachment(context: Context, downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri))
            }
        }
        cursor.close()
    }

    private fun openDownloadedAttachment(context: Context, uri: Uri?) {
        var attachmentUri = uri
        if (attachmentUri != null) {
            if (ContentResolver.SCHEME_FILE == attachmentUri.scheme) {
                val file = File(attachmentUri.path ?: return)
                attachmentUri = getApkUri(context, file)
            }

            val openAttachmentIntent = Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                clipData = ClipData.newUri(context.contentResolver, "Downloaded APK", attachmentUri)
                setDataAndType(attachmentUri, APK_MIME_TYPE)
            }

            try {
                context.startActivity(openAttachmentIntent)
            } catch (e: ActivityNotFoundException) {
                Timber.e(e, "No activity found to install downloaded APK.")
            }
        }
    }

    private fun getApkUri(context: Context, file: File): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, APK_FILE_PROVIDER_AUTHORITY, file)
        } else {
            Uri.fromFile(file)
        }
}
