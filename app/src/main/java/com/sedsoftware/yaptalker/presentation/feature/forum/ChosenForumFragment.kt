package com.sedsoftware.yaptalker.presentation.feature.forum

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import androidx.core.os.bundleOf
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.forum.adapter.ChosenForumAdapter
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_chosen_forum.*
import java.util.Locale
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_chosen_forum)
class ChosenForumFragment : BaseFragment(), ChosenForumView {

  companion object {
    fun getNewInstance(data: Pair<Int, String>): ChosenForumFragment =
      ChosenForumFragment().apply {
        arguments = bundleOf(FORUM_ID_KEY to data.first, FORUM_NAME_KEY to data.second)
      }

    private const val FORUM_ID_KEY = "FORUM_ID_KEY"
    private const val FORUM_NAME_KEY = "FORUM_NAME_KEY"
  }

  @Inject
  lateinit var forumAdapter: ChosenForumAdapter

  @Inject
  @InjectPresenter
  lateinit var presenter: ChosenForumPresenter

  @ProvidePresenter
  fun provideForumPresenter() = presenter

  private val currentForumId: Int by lazy {
    arguments?.getInt(FORUM_ID_KEY) ?: 0
  }

  private val currentForumName: String by lazy {
    arguments?.getString(FORUM_NAME_KEY) ?: ""
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(forum_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumAdapter
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }

    forum_refresh_layout.setIndicatorColorScheme()

    subscribeViews()
  }

  override fun showLoadingIndicator() {
    forum_refresh_layout.isRefreshing = true
  }

  override fun hideLoadingIndicator() {
    forum_refresh_layout.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {
    messagesDelegate.showMessageError(message)
  }

  override fun updateCurrentUiState() {
    setCurrentAppbarTitle(currentForumName)
    setCurrentNavDrawerItem(NavigationSection.FORUMS)
  }

  override fun addTopicItem(item: YapEntity) {
    forumAdapter.addTopicItem(item)
  }

  override fun clearTopicsList() {
    forumAdapter.clearTopicsList()
  }

  override fun initiateForumLoading() {
    presenter.loadForum(currentForumId)
  }

  override fun scrollToViewTop() {
    forum_topics_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showCantLoadPageMessage(page: Int) {
    messagesDelegate.showMessageWarning(
      String.format(Locale.getDefault(), string(R.string.navigation_page_not_available), page))
  }

  override fun showPageSelectionDialog() {
    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
        .title(R.string.navigation_go_to_page_title)
        .inputType(InputType.TYPE_CLASS_NUMBER)
        .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
          presenter.goToChosenPage(input.toString().toInt())
        })
        .show()
    }
  }

  private fun subscribeViews() {

    RxSwipeRefreshLayout
      .refreshes(forum_refresh_layout)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { presenter.loadForum(currentForumId) }
  }
}
