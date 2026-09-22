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
            uq = textTransformer.transformRankToFormattedText(profile.uq),
            signature = textTransformer.transformHtmlToSpanned(profile.signature),
            registerDate = profile.registerDate,
            timeZone = profile.timeZone,
            birthDate = profile.birthDate,
            location = profile.location,
            sex = profile.sex,
            messagesCount = profile.messagesCount
        )
}
