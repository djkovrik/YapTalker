package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import com.sedsoftware.yaptalker.presentation.mappers.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel
import javax.inject.Inject

/**
 * Mapper class used to transform user profile entity from the domain layer into YapEntity in the presentation layer.
 */
class UserProfileModelMapper @Inject constructor(
    private val textTransformer: TextTransformer) {

  fun transform(profile: BaseEntity): YapEntity {

    profile as UserProfile

    return UserProfileModel(
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
        website = textTransformer.transformHtmlToSpanned(profile.website),
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
}
