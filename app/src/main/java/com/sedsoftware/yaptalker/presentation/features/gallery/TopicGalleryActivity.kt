package com.sedsoftware.yaptalker.presentation.features.gallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastSuccess
import com.sedsoftware.yaptalker.presentation.extensions.visibleItemPosition
import com.sedsoftware.yaptalker.presentation.features.gallery.adapter.TopicGalleryAdapter
import com.sedsoftware.yaptalker.presentation.features.gallery.adapter.TopicGalleryLoadMoreClickListener
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import kotlinx.android.synthetic.main.activity_topic_gallery.*
import kotlinx.android.synthetic.main.include_main_appbar_transparent.*
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
    private const val STORAGE_WRITE_PERMISSION = 0
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

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_image_display, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val itemPosition = topic_gallery.visibleItemPosition()

    if (itemPosition == -1) {
      return false
    }

    val imageUrl = galleryAdapter.items[itemPosition]

    return when (item.itemId) {
      R.id.action_share -> {
        presenter.shareImage(imageUrl.url)
        true
      }
      R.id.action_save -> {
        checkPermissionAndSaveImage(imageUrl.url)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
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

  override fun scrollToFirstNewImage(newImagesOffset: Int) {
    topic_gallery.smoothScrollToPosition( galleryAdapter.itemCount - newImagesOffset)
  }

  override fun lastPageReached() {
    galleryAdapter.isLastPageVisible = true
  }

  override fun fileSavedMessage(filepath: String) {
    String.format(Locale.getDefault(), stringRes(R.string.msg_file_saved), filepath).apply {
      toastSuccess(this)
    }
  }

  override fun fileNotSavedMessage() {
    toastError(stringRes(R.string.msg_file_not_saved))
  }

  override fun onLoadMoreClicked() {
    presenter.loadMoreImages()
  }

  private fun checkPermissionAndSaveImage(imageUrl: String) {
    if (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        STORAGE_WRITE_PERMISSION
      )
    } else {
      presenter.saveImage(imageUrl)
    }
  }
}
