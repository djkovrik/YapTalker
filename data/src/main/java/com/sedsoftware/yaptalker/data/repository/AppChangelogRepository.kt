package com.sedsoftware.yaptalker.data.repository

import android.content.Context
import android.os.Build
import com.sedsoftware.yaptalker.domain.repository.ChangelogRepository
import io.reactivex.Single
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject


class AppChangelogRepository @Inject constructor(
  private val context: Context
) : ChangelogRepository {

  override fun getChangelog(): Single<String> =
    Single.create { emitter ->

      val resources = context.resources
      val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales.get(0)
      } else {
        //noinspection deprecation
        resources.configuration.locale
      }

      val stream: InputStream?
      var result: String? = null

      try {
        stream = if (locale.country == "ru_RU") {
          resources.assets.open("CHANGELOG_RU.md")
        } else {
          resources.assets.open("CHANGELOG.md")
        }
        result = readStream(stream)
      } catch (e: Exception) {
        emitter.onError(e)
      }

      result?.let { emitter.onSuccess(it) } ?: emitter.onSuccess("")
    }

  private fun readStream(inputStream: InputStream?): String? {

    var out: String? = null

    if (inputStream != null) {
      var reader: BufferedReader? = null
      try {
        reader = BufferedReader(InputStreamReader(inputStream))
        val builder = StringBuilder()
        var line: String
        while (true) {
          line = reader.readLine() ?: break
          builder.append(line).append('\n')
        }
        out = builder.toString()
      } catch (e: IOException) {
      } finally {
        if (reader != null) {
          try {
            reader.close()
          } catch (e: IOException) {
            // no op
          }
        }
      }
    }

    return out
  }
}
