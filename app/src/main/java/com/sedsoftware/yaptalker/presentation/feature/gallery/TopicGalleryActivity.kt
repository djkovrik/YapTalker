package com.sedsoftware.yaptalker.presentation.feature.gallery

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
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import com.sedsoftware.yaptalker.presentation.extensions.snackSuccess
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.visibleItemPosition
import com.sedsoftware.yaptalker.presentation.feature.gallery.adapter.TopicGalleryAdapter
import com.sedsoftware.yaptalker.presentation.feature.gallery.adapter.TopicGalleryLoadMoreClickListener
import com.sedsoftware.yaptalker.presentation.feature.topic.GalleryInitialState
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import kotlinx.android.synthetic.main.activity_topic_gallery.*
import kotlinx.android.synthetic.main.include_main_appbar_transparent.*
import java.util.Locale
import javax.inject.Inject

@LayoutResource(R.layout.activity_topic_gallery)
class TopicGalleryActivity : BaseActivity(), TopicGalleryView, TopicGalleryLoadMoreClickListener {

  companion object {
    fun getIntent(ctx: Context, initialState: GalleryInitialState): Intent {
      val intent = Intent(ctx, TopicGalleryActivity::class.java)
      intent.putExtra(GALLERY_INITIAL_STATE_KEY, initialState)
      return intent
    }

    private const val GALLERY_INITIAL_STATE_KEY = "CURRENT_PAGE_KEY"
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

  val galleryInitialState: GalleryInitialState by lazy {
    intent.getParcelableExtra(GALLERY_INITIAL_STATE_KEY) as GalleryInitialState?
        ?: throw  IllegalArgumentException("Gallery initial state must be provided via arguments")
  }

  private var savingImageUrl = ""

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
    snackError(message)
  }

  override fun appendImages(images: List<YapEntity>) {
    galleryAdapter.addList(images.map { it as SinglePostGalleryImageModel })
  }

  override fun updateCurrentUiState(title: String) {
    supportActionBar?.title = String.format(Locale.getDefault(), titleTemplate, title)
  }

  override fun scrollToFirstNewImage(newImagesOffset: Int) {
    topic_gallery.smoothScrollToPosition(galleryAdapter.itemCount - newImagesOffset)
  }

  override fun scrollToSelectedImage(imageUrl: String) {
    val element = galleryAdapter.items.find { it.url == imageUrl }
    val position = galleryAdapter.items.indexOf(element)

    if (position != -1) {
      topic_gallery.scrollToPosition(position)
    }
  }

  override fun lastPageReached() {
    galleryAdapter.isLastPageVisible = true
  }

  override fun fileSavedMessage(filepath: String) {
    String.format(Locale.getDefault(), stringRes(R.string.msg_file_saved), filepath).apply {
      snackSuccess(this)
    }
  }

  override fun fileNotSavedMessage() {
    snackError(stringRes(R.string.msg_file_not_saved))
  }

  override fun onLoadMoreClicked() {
    presenter.loadMoreImages()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when (requestCode) {
      STORAGE_WRITE_PERMISSION -> {
        presenter.saveImage(savingImageUrl)
      }
    }
  }

  private fun checkPermissionAndSaveImage(imageUrl: String) {
    if (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      savingImageUrl = imageUrl
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
