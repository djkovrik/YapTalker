package com.sedsoftware.yaptalker.presentation.features.userprofile

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel
import kotlinx.android.synthetic.main.fragment_user_profile.profile_bayans
import kotlinx.android.synthetic.main.fragment_user_profile.profile_birth_date
import kotlinx.android.synthetic.main.fragment_user_profile.profile_email
import kotlinx.android.synthetic.main.fragment_user_profile.profile_group
import kotlinx.android.synthetic.main.fragment_user_profile.profile_icq
import kotlinx.android.synthetic.main.fragment_user_profile.profile_interests
import kotlinx.android.synthetic.main.fragment_user_profile.profile_location
import kotlinx.android.synthetic.main.fragment_user_profile.profile_messages
import kotlinx.android.synthetic.main.fragment_user_profile.profile_messages_day
import kotlinx.android.synthetic.main.fragment_user_profile.profile_photo
import kotlinx.android.synthetic.main.fragment_user_profile.profile_photo_card
import kotlinx.android.synthetic.main.fragment_user_profile.profile_registered
import kotlinx.android.synthetic.main.fragment_user_profile.profile_rewards
import kotlinx.android.synthetic.main.fragment_user_profile.profile_sex
import kotlinx.android.synthetic.main.fragment_user_profile.profile_sign
import kotlinx.android.synthetic.main.fragment_user_profile.profile_status
import kotlinx.android.synthetic.main.fragment_user_profile.profile_time_zone
import kotlinx.android.synthetic.main.fragment_user_profile.profile_topics_today
import kotlinx.android.synthetic.main.fragment_user_profile.profile_uq
import kotlinx.android.synthetic.main.fragment_user_profile.profile_web_site
import java.util.Locale
import javax.inject.Inject

class UserProfileFragment : BaseFragment(), UserProfileView {

  companion object {
    fun getNewInstance(userId: Int): UserProfileFragment {
      val fragment = UserProfileFragment()
      val args = Bundle()
      args.putInt(USER_ID_KEY, userId)
      fragment.arguments = args
      return fragment
    }

    private const val USER_ID_KEY = "USER_ID_KEY"
  }

  override val layoutId: Int
    get() = R.layout.fragment_user_profile

  @Inject
  @InjectPresenter
  lateinit var presenter: UserProfilePresenter

  @ProvidePresenter
  fun provideUserProfilePresenter() = presenter

  private val userId by lazy {
    arguments?.getInt(USER_ID_KEY) ?: 0
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    presenter.loadUserProfile(userId)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun displayProfile(profile: UserProfileModel) {
    profile_uq.text = profile.uq
    profile_sign.text = profile.signature
    profile_rewards.text = profile.rewards

    context?.let { ctx ->
      profile_group.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_group), profile.group)
      profile_status.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_status), profile.status)
      profile_registered.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_registered), profile.registerDate)
      profile_time_zone.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_time_zone), profile.timeZone)
      profile_birth_date.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_birth_date), profile.birthDate)
      profile_location.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_location), profile.location)
      profile_interests.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_interests), profile.interests)
      profile_sex.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_sex), profile.sex)
      profile_messages.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_messages), profile.messagesCount)
      profile_messages_day.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_messages_day), profile.messsagesPerDay)
      profile_bayans.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_bayans), profile.bayans)
      profile_topics_today.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_topics_today), profile.todayTopics)
      profile_email.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_email), profile.email)
      profile_icq.text = String.format(Locale.getDefault(),
          ctx.stringRes(R.string.profile_icq), profile.icq)
    }

    profile_web_site.text = profile.website
    profile_web_site.movementMethod = LinkMovementMethod.getInstance()

    if (profile.photo.isNotEmpty()) {
      profile_photo.loadFromUrl(profile.photo)
    } else {
      profile_photo_card.hideView()
    }
  }

  override fun updateCurrentUiState(title: String) {
    presenter.setAppbarTitle(title)
    presenter.setNavDrawerItem(NavigationSection.FORUMS)
  }
}
