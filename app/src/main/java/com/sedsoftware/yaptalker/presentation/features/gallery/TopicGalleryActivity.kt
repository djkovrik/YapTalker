package com.sedsoftware.yaptalker.presentation.features.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import timber.log.Timber
import javax.inject.Inject

@LayoutResource(R.layout.activity_topic_gallery)
class TopicGalleryActivity : BaseActivity(), TopicGalleryView {

  companion object {
    fun getIntent(ctx: Context, triple: Triple<Int, Int, Int>): Intent {
      val intent = Intent(ctx, TopicGalleryActivity::class.java)
      intent.putExtra(FORUM_ID_KEY, triple.first)
      intent.putExtra(TOPIC_ID_KEY, triple.second)
      intent.putExtra(CURRENT_PAGE_KEY, triple.third)
      return intent
    }

    private const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val TOPIC_ID_KEY = "TOPIC_ID_KEY"
    private const val CURRENT_PAGE_KEY = "CURRENT_PAGE_KEY"
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: TopicGalleryPresenter

  @ProvidePresenter
  fun provideGalleryPresenter() = presenter

  private val forumId: Int by lazy {
    intent.getIntExtra(FORUM_ID_KEY, 0)
  }

  private val topicId: Int by lazy {
    intent.getIntExtra(TOPIC_ID_KEY, 0)
  }

  private val currentPage: Int by lazy {
    intent.getIntExtra(CURRENT_PAGE_KEY, 0)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("forumId = $forumId, topicId = $topicId, currentPage = $currentPage")
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }
}
