package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.SignInService
import io.reactivex.Observable
import javax.inject.Inject

class YapSignInService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ServerResponseMapper
) : SignInService {

  companion object {
    private const val LOGIN_COOKIE_DATE = 1
    private const val LOGIN_REFERRER = "http://www.yaplakal.com/forum/"
    private const val LOGIN_SUBMIT = "Вход"
  }

  override fun requestSignIn(userLogin: String, userPassword: String, anonymously: Boolean): Observable<BaseEntity> =
      dataLoader
          .signIn(
              cookieDate = LOGIN_COOKIE_DATE,
              privacy = anonymously,
              password = userPassword,
              userName = userLogin,
              referer = LOGIN_REFERRER,
              submit = LOGIN_SUBMIT,
              userKey = "$userLogin${System.currentTimeMillis()}".toMd5()
          )
          .map { response -> dataMapper.transform(response) }

  @Suppress("MagicNumber")
  private fun String.toMd5(): String {

    val digest = java.security.MessageDigest.getInstance("MD5")
    digest.update(this.toByteArray())
    val messageDigest = digest.digest()
    val hexString = StringBuffer()

    for (i in 0 until messageDigest.size) {
      var hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
      while (hex.length < 2)
        hex = "0" + hex
      hexString.append(hex)
    }
    return hexString.toString()
  }
}
