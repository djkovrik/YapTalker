package com.sedsoftware.data.entity.mappers

import com.sedsoftware.data.entity.UserProfileParsed
import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.base.UserProfile

/**
 * Mapper class used to transform parsed user profile from the data layer into YapEntity in the domain layer.
 */
class UserProfileMapper {

  fun transform(profile: UserProfileParsed): YapEntity =
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
