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
            status = from.status.orEmpty(),
            uq = from.rankValue?.toIntOrNull() ?: 0,
            signature = "",
            rewards = "",
            registerDate = formatJoinedDate(from.joined),
            timeZone = from.timeZone.orEmpty(),
            website = "",
            birthDate = from.birthday.orEmpty(),
            location = from.location.orEmpty(),
            interests = "",
            sex = from.sex.orEmpty(),
            messagesCount = from.posts.orEmpty(),
            messsagesPerDay = "",
            bayans = "",
            todayTopics = "",
            email = from.email.orEmpty(),
            icq = ""
        )

    private fun formatJoinedDate(joined: String?): String {
        if (joined.isNullOrBlank()) return ""

        return runCatching {
            val sourceFormat = SimpleDateFormat(SERVER_DATE_PATTERN, Locale.US)
            val targetFormat = SimpleDateFormat(PROFILE_DATE_PATTERN, Locale.getDefault())
            sourceFormat.parse(joined)?.let(targetFormat::format) ?: joined
        }.getOrDefault(joined)
    }

    private companion object {
        const val SERVER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
        const val PROFILE_DATE_PATTERN = "dd.MM.yyyy"
    }
}
