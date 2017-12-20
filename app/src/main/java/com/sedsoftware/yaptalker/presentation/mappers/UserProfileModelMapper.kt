package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel

/**
 * Mapper class used to transform user profile entity from the domain layer into YapEntity in the presentation layer.
 */
class UserProfileModelMapper {

  fun transform(profile: BaseEntity): YapEntity {

    profile as UserProfile

    return UserProfileModel(
        nickname = profile.nickname,
        avatar = profile.avatar,
        photo = profile.photo,
        group = profile.group,
        status = profile.status,
        uq = profile.uq,
        signature = profile.signature,
        rewards = profile.rewards,
        registerDate = profile.registerDate,
        timeZone = profile.timeZone,
        website = profile.website,
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
