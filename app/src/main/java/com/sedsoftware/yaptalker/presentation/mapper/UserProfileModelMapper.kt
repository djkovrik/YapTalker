package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel
import io.reactivex.functions.Function
import javax.inject.Inject

class UserProfileModelMapper @Inject constructor(
    private val textTransformer: TextTransformer
) : Function<UserProfile, UserProfileModel> {

    override fun apply(profile: UserProfile): UserProfileModel =
        UserProfileModel(
            nickname = profile.nickname,
            avatar = profile.avatar,
            photo = profile.photo,
            group = profile.group,
            status = profile.status,
            uq = textTransformer.transformRankToFormattedText(profile.uq),
            signature = textTransformer.transformHtmlToSpanned(profile.signature),
            rewards = textTransformer.transformHtmlToSpanned(profile.rewards),
            registerDate = profile.registerDate,
            timeZone = profile.timeZone,
            website = textTransformer.transformWebsiteToSpanned(profile.website),
            birthDate = profile.birthDate,
            location = profile.location,
            interests = profile.interests,
            sex = profile.sex,
            messagesCount = profile.messagesCount,
            messsagesPerDay = profile.messsagesPerDay,
            bayans = profile.bayans,
            todayTopics = profile.todayTopics,
            email = profile.email,
            icq = profile.icq
        )
}
