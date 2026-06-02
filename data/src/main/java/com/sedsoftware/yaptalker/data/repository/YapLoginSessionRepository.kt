package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.exception.RequestErrorException
import com.sedsoftware.yaptalker.data.mapper.LoginSessionInfoMapper
import com.sedsoftware.yaptalker.data.mapper.ServerResponseMapper
import com.sedsoftware.yaptalker.data.network.site.YapApi
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.network.site.model.UserSmall
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.CookieStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class YapLoginSessionRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val yapApi: YapApi,
    private val cookieStorage: CookieStorage,
    private val settings: Settings,
    private val dataMapper: LoginSessionInfoMapper,
    private val responseMapper: ServerResponseMapper,
    private val schedulers: SchedulersProvider
) : LoginSessionRepository {

    companion object {
        private const val LOGIN_COOKIE_DATE = 1
        private const val LOGIN_REFERRER = "https://www.yaplakal.com/forum/"
        private const val LOGIN_SUBMIT = "Вход"

        private const val SIGN_OUT_SUCCESS_MARKER = "Вы вышли"
        private const val SIGN_IN_SUCCESS_MARKER = "Спасибо"
    }

    override fun getLoginSessionInfo(): Single<LoginSessionInfo> =
        yapApi
            .settings()
            .map { response -> response.user?.toLoginSessionInfo() ?: emptyLoginSessionInfo() }
            .doOnSuccess { info ->
                if (info.nickname.isNotEmpty() && info.sessionId.isNotEmpty()) {
                    settings.saveCachedLoginSessionInfo(info)
                }
            }
            .onErrorResumeNext {
                val cachedInfo = settings.getCachedLoginSessionInfo()
                if (cookieStorage.getCookie().isNotEmpty() && cachedInfo != null) {
                    Single.just(cachedInfo)
                } else if (settings.getLogin().isNotEmpty() && settings.getPassword().isNotEmpty()) {
                    restoreApiSession()
                } else {
                    dataLoader.loadAuthorizedUserInfo().map(dataMapper)
                }
            }
            .subscribeOn(schedulers.io())

    override fun requestSignIn(userLogin: String, userPassword: String, anonymously: Boolean): Completable =
        userLogin.trim().let { normalizedLogin ->
            dataLoader
                .signIn(
                    cookieDate = LOGIN_COOKIE_DATE,
                    privacy = anonymously,
                    password = userPassword,
                    userName = normalizedLogin,
                    referer = LOGIN_REFERRER,
                    submit = LOGIN_SUBMIT,
                    userKey = "$normalizedLogin${System.currentTimeMillis()}".toMd5()
                )
                .map(responseMapper)
                .flatMapCompletable { response ->
                    if (response.text.contains(SIGN_IN_SUCCESS_MARKER)) {
                        Completable.complete()
                    } else {
                        Completable.error(RequestErrorException("Unable to complete sign in request."))
                    }
                }
                .subscribeOn(schedulers.io())
        }

    override fun requestSignOut(userKey: String): Completable =
        yapApi
            .logout()
            .ignoreElement()
            .onErrorResumeNext {
                dataLoader
                    .signOut(userKey)
                    .map(responseMapper)
                    .flatMapCompletable { response ->
                        if (response.text.contains(SIGN_OUT_SUCCESS_MARKER)) {
                            Completable.complete()
                        } else {
                            Completable.error(RequestErrorException("Unable to complete sign out request."))
                        }
                    }
            }
            .onErrorComplete()
            .doOnComplete(::clearLocalSession)
            .subscribeOn(schedulers.io())

    override fun requestSignInWithApi(userLogin: String, userPassword: String): Completable =
        userLogin.trim().let { normalizedLogin ->
            yapApi
                .authUser(
                    name = normalizedLogin,
                    password = userPassword
                )
                .doOnSubscribe {
                    cookieStorage.clearCookie()
                    settings.clearCachedLoginSessionInfo()
                }
                .flatMapCompletable { response ->
                    val sid = response.user?.sid.orEmpty()
                    val authKey = response.user?.authKey.orEmpty()
                    val userId = response.user?.id.orEmpty()
                    val userName = response.user?.name.orEmpty()
                    if (sid.isEmpty() || authKey.isEmpty() || userId == "0" || userName.equals("Guest", ignoreCase = true)) {
                        Completable.error(RequestErrorException("API login response did not contain authorized user."))
                    } else {
                        cookieStorage.saveCookie("SID=$sid")
                        response.user?.toLoginSessionInfo()?.let(settings::saveCachedLoginSessionInfo)
                        Completable.complete()
                    }
                }
                .subscribeOn(schedulers.io())
        }

    private fun clearLocalSession() {
        cookieStorage.clearCookie()
        settings.clearCachedLoginSessionInfo()
        settings.saveLogin("")
        settings.savePassword("")
    }

    private fun restoreApiSession(): Single<LoginSessionInfo> =
        yapApi
            .authUser(
                name = settings.getLogin(),
                password = settings.getPassword()
            )
            .map { response ->
                val sid = response.user?.sid.orEmpty()
                val authKey = response.user?.authKey.orEmpty()
                val userId = response.user?.id.orEmpty()
                val userName = response.user?.name.orEmpty()
                if (sid.isEmpty() || authKey.isEmpty() || userId == "0" || userName.equals("Guest", ignoreCase = true)) {
                    throw RequestErrorException("API login response did not contain authorized user.")
                }
                cookieStorage.saveCookie("SID=$sid")
                response.user?.toLoginSessionInfo()?.also(settings::saveCachedLoginSessionInfo) ?: emptyLoginSessionInfo()
            }

    private fun UserSmall.toLoginSessionInfo(): LoginSessionInfo =
        LoginSessionInfo(
            nickname = name.orEmpty(),
            profileLink = id.orEmpty().let { userId ->
                if (userId.isEmpty()) "" else "/members/member$userId.html"
            },
            title = rank?.toString().orEmpty(),
            uq = rank ?: 0,
            avatar = avatarUrl.orEmpty(),
            mailCounter = newMails.orEmpty(),
            sessionId = authKey.orEmpty()
        )

    private fun emptyLoginSessionInfo(): LoginSessionInfo =
        LoginSessionInfo(
            nickname = "",
            profileLink = "",
            title = "",
            uq = 0,
            avatar = "",
            mailCounter = "",
            sessionId = ""
        )

    @Suppress("MagicNumber")
    private fun String.toMd5(): String {
        val digest = java.security.MessageDigest.getInstance("MD5")
        digest.update(this.toByteArray())
        val messageDigest = digest.digest()
        val hexString = StringBuffer()

        for (i in 0 until messageDigest.size) {
            var hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
            while (hex.length < 2)
                hex = "0$hex"
            hexString.append(hex)
        }
        return hexString.toString()
    }

}
