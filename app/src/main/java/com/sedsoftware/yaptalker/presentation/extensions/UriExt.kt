package com.sedsoftware.yaptalker.presentation.extensions

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore.MediaColumns


fun Uri.getFilePath(contentResolver: ContentResolver): String {
  val filePath: String
  val filePathColumn = arrayOf(MediaColumns.DATA)

  val cursor = contentResolver.query(this, filePathColumn, null, null, null)
  cursor.moveToFirst()

  val columnIndex = cursor.getColumnIndex(filePathColumn[0])
  filePath = cursor.getString(columnIndex)
  cursor.close()
  return filePath
}
