package com.sedsoftware.yaptalker.features.userprofile

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.hideView
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.data.model.UserProfile
import com.sedsoftware.yaptalker.features.base.BaseController
import kotlinx.android.synthetic.main.controller_user_profile.view.*
import java.util.Locale

class UserProfileController(val bundle: Bundle) : BaseController(bundle), UserProfileView {

  companion object {
    const val USER_ID_KEY = "USER_ID_KEY"
  }

  private val userId by lazy {
    bundle.getInt(USER_ID_KEY)
  }

  override val controllerLayoutId: Int
    get() = R.layout.controller_user_profile

  @InjectPresenter
  lateinit var userProfilePresenter: UserProfilePresenter

  override fun onViewBound(view: View, savedViewState: Bundle?) {
    userProfilePresenter.loadUserProfile(userId)
  }

  override fun subscribeViews(parent: View) {
  }

  override fun showLoading() {
  }

  override fun showContent() {
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun setAppbarTitle(title: String) {
    userProfilePresenter.setAppbarTitle(title)
  }

  override fun displayProfile(profile: UserProfile) {

    view?.let {
      it.apply {
        profile_group.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_group), profile.group)
        profile_status.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_status), profile.status)
        profile_uq.ratingText = profile.uq
        profile_sign.textFromHtml(profile.signature)
        profile_rewards.textFromHtml(profile.rewards)
        profile_registered.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_registered), profile.registerDate)
        profile_time_zone.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_time_zone), profile.timeZone)

        if (profile.website.startsWith("http")) {
          val template = context.stringRes(R.string.profile_web_site)
          val html = String.format(Locale.getDefault(), template, profile.website)
          profile_web_site.textFromHtml(html)
        }

        profile_birth_date.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_birth_date), profile.birthDate)
        profile_location.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_location), profile.location)
        profile_interests.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_interests), profile.interests)
        profile_sex.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_sex), profile.sex)
        profile_messages.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_messages), profile.messagesCount)
        profile_messages_day.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_messages_day), profile.messsagesPerDay)
        profile_bayans.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_bayans), profile.bayans)
        profile_topics_today.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_topics_today), profile.todayTopics)
        profile_email.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_email), profile.email)
        profile_icq.text = String.format(Locale.getDefault(), context.stringRes(R.string.profile_icq), profile.icq)

        if (profile.photo.isNotEmpty()) {
          profile_photo.loadFromUrl(profile.photo)
        } else {
          profile_photo_card.hideView()
        }
      }
    }
  }
}
