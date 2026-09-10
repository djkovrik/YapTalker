package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.network.site.model.UserProfileApi
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import io.reactivex.functions.Function
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class UserProfileMapper @Inject constructor() : Function<UserProfileApi, UserProfile> {

    override fun apply(from: UserProfileApi): UserProfile =
        UserProfile(
            nickname = from.name.orEmpty(),
            avatar = from.avatarUrl.orEmpty(),
            photo = from.photoUrl.orEmpty(),
            group = from.groupTitle.orEmpty(),
            uq = from.rankValue?.toIntOrNull() ?: 0,
            signature = from.status.orEmpty(),
            registerDate = formatDate(from.joined, SERVER_DATE_PATTERN),
            timeZone = from.timeZone.orEmpty(),
            birthDate = formatDate(from.birthday, BIRTH_DATE_PATTERN),
            location = from.location.orEmpty(),
            sex = from.sex.orEmpty(),
            messagesCount = from.posts.orEmpty()
        )

    private fun formatDate(value: String?, sourcePattern: String): String {
        if (value.isNullOrBlank()) return ""

        return runCatching {
            val sourceFormat = SimpleDateFormat(sourcePattern, Locale.US).apply {
                isLenient = false
            }
            val targetFormat = SimpleDateFormat(PROFILE_DATE_PATTERN, Locale.getDefault())
            sourceFormat.parse(value)?.let(targetFormat::format) ?: value
        }.getOrDefault(value)
    }

    private companion object {
        const val SERVER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
        const val BIRTH_DATE_PATTERN = "yyyy-MM-dd"
        const val PROFILE_DATE_PATTERN = "dd.MM.yyyy"
    }
}
