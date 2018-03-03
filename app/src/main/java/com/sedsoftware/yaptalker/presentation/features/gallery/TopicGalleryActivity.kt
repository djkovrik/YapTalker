package com.sedsoftware.yaptalker.presentation.features.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.features.gallery.adapter.TopicGalleryAdapter
import com.sedsoftware.yaptalker.presentation.features.gallery.adapter.TopicGalleryLoadMoreClickListener
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import kotlinx.android.synthetic.main.activity_topic_gallery.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import java.util.Locale
import javax.inject.Inject

@LayoutResource(R.layout.activity_topic_gallery)
class TopicGalleryActivity : BaseActivity(), TopicGalleryView, TopicGalleryLoadMoreClickListener {

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

  private val titleTemplate: String by lazy {
    stringRes(R.string.navigation_gallery_page)
  }

  @Inject
  lateinit var galleryAdapter: TopicGalleryAdapter

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

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    with(topic_gallery) {
      val linearLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
      layoutManager = linearLayout
      adapter = galleryAdapter
      setHasFixedSize(true)
    }

    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(topic_gallery)

    presenter.loadTopicGallery(forumId, topicId, currentPage)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun appendImages(images: List<YapEntity>) {
    galleryAdapter.addList(images.map { it as SinglePostGalleryImageModel })
  }

  override fun updateCurrentUiState(title: String) {
    supportActionBar?.title = String.format(Locale.getDefault(), titleTemplate, title)
  }

  override fun onLoadMoreClicked() {
    presenter.loadMoreImages()
  }
}
