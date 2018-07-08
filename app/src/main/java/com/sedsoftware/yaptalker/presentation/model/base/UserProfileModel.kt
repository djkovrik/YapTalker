package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned

class UserProfileModel(
    val nickname: String,
    val avatar: String,
    val photo: String,
    val group: String,
    val status: String,
    val uq: Spanned,
    val signature: Spanned,
    val rewards: Spanned,
    val registerDate: String,
    val timeZone: String,
    val website: Spanned,
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
)
