package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.UserProfileParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import javax.inject.Inject

/**
 * Mapper class used to transform parsed user profile from the data layer into BaseEntity in the domain layer.
 */
class UserProfileMapper @Inject constructor() {

  fun transform(profile: UserProfileParsed): BaseEntity =
      UserProfile(
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
