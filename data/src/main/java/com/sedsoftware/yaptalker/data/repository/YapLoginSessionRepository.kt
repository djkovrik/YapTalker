package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.LoginSessionInfoMapper
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapLoginSessionRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: LoginSessionInfoMapper,
    private val responseMapper: ServerResponseMapper
) : LoginSessionRepository {

  companion object {
    private const val LOGIN_COOKIE_DATE = 1
    private const val LOGIN_REFERRER = "http://www.yaplakal.com/forum/"
    private const val LOGIN_SUBMIT = "Вход"
  }

  override fun getLoginSessionInfo(): Observable<BaseEntity> =
      dataLoader
          .loadAuthorizedUserInfo()
          .map { parsedSessionInfo -> dataMapper.transform(parsedSessionInfo) }

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
          .map { response -> responseMapper.transform(response) }

  override fun requestSignOut(userKey: String): Observable<BaseEntity> =
      dataLoader
          .signOut(userKey)
          .map { response -> responseMapper.transform(response) }

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
