package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.UserProfileParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.UserProfile
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform parsed user profile from the data layer into BaseEntity in the domain layer.
 */
class UserProfileMapper @Inject constructor() : Function<UserProfileParsed, BaseEntity> {

  override fun apply(from: UserProfileParsed): BaseEntity =
    UserProfile(
      nickname = from.nickname,
      avatar = from.avatar,
      photo = from.photo,
      group = from.group,
      status = from.status,
      uq = from.uq.toInt(),
      signature = from.signature,
      rewards = from.rewards,
      registerDate = from.registerDate,
      timeZone = from.timeZone,
      website = from.website,
      birthDate = from.birthDate,
      location = from.location,
      interests = from.interests,
      sex = from.sex,
      messagesCount = from.messagesCount,
      messsagesPerDay = from.messsagesPerDay,
      bayans = from.bayans,
      todayTopics = from.todayTopics,
      email = from.email,
      icq = from.icq
    )
}
