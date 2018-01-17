package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

class UserProfile(
  val nickname: String,
  val avatar: String,
  val photo: String,
  val group: String,
  val status: String,
  val uq: Int,
  val signature: String,
  val rewards: String,
  val registerDate: String,
  val timeZone: String,
  val website: String,
  val birthDate: String,
  val location: String,
  val interests: String,
  val sex: String,
  val messagesCount: String,
  val messsagesPerDay: String,
  val bayans: String,
  val todayTopics: String,
  val email: String,
  val icq: String
) : BaseEntity
