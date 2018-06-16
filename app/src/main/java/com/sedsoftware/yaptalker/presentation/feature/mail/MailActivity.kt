package com.sedsoftware.yaptalker.presentation.feature.mail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import kotlinx.android.synthetic.main.include_main_appbar.toolbar
import javax.inject.Inject

@LayoutResource(value = R.layout.activity_mail)
class MailActivity : BaseActivity(), MailView {

  companion object {
    fun getIntent(ctx: Context): Intent =
      Intent(ctx, MailActivity::class.java)
  }

  @Inject
  lateinit var messagesDelegate: MessagesDelegate

  @Inject
  @InjectPresenter
  lateinit var presenter: MailPresenter

  @ProvidePresenter
  fun provideImagePresenter() = presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun showErrorMessage(message: String) {
  }

  override fun showLoadingIndicator() {
  }

  override fun hideLoadingIndicator() {
  }

  override fun updateCurrentUiState() {
  }
}
