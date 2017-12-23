package com.sedsoftware.yaptalker.presentation.features.forum

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastWarning
import com.sedsoftware.yaptalker.presentation.features.forum.adapter.ChosenForumAdapter
import com.sedsoftware.yaptalker.presentation.features.forum.adapter.ForumNavigationClickListener
import com.sedsoftware.yaptalker.presentation.features.forum.adapter.TopicItemClickListener
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_chosen_forum.*
import java.util.Locale
import javax.inject.Inject

class ChosenForumFragment : BaseFragment(), ChosenForumView, TopicItemClickListener, ForumNavigationClickListener {

  companion object {
    private const val FORUM_ID_KEY = "FORUM_ID_KEY"

    fun getNewInstance(forumId: Int): ChosenForumFragment {
      val fragment = ChosenForumFragment()
      val args = Bundle()
      args.putInt(FORUM_ID_KEY, forumId)
      fragment.arguments = args
      return fragment
    }
  }

  override val layoutId: Int
    get() = R.layout.fragment_chosen_forum

  @Inject
  lateinit var settings: SettingsManager

  @Inject
  @InjectPresenter
  lateinit var presenter: ChosenForumPresenter

  @ProvidePresenter
  fun provideForumPresenter() = presenter

  private val currentForumId: Int by lazy {
    arguments?.getInt(FORUM_ID_KEY) ?: 0
  }

  private lateinit var forumAdapter: ChosenForumAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    forumAdapter = ChosenForumAdapter(this, this, settings)
    forumAdapter.setHasStableIds(true)

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
    toastError(message)
  }

  override fun addTopicItem(entity: YapEntity) {
    forumAdapter.addTopicItem(entity)
  }

  override fun clearTopicsList() {
    forumAdapter.clearTopicsList()
  }

  override fun updateCurrentUiState(forumTitle: String) {
    presenter.setAppbarTitle(forumTitle)
    presenter.setNavDrawerItem(NavigationSection.FORUMS)
  }

  override fun initiateForumLoading() {
    presenter.loadForum(currentForumId)
  }

  override fun scrollToViewTop() {
    forum_topics_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showCantLoadPageMessage(page: Int) {
    context?.stringRes(R.string.navigation_page_not_available)?.let { template ->
      toastWarning(String.format(Locale.getDefault(), template, page))
    }
  }

  override fun onTopicClick(topicId: Int) {
    presenter.navigateToChosenTopic(Triple(currentForumId, topicId, 0))
  }

  override fun onGoToFirstPageClick() {
    presenter.goToFirstPage()
  }

  override fun onGoToLastPageClick() {
    presenter.goToLastPage()
  }

  override fun onGoToPreviousPageClick() {
    presenter.goToPreviousPage()
  }

  override fun onGoToNextPageClick() {
    presenter.goToNextPage()
  }

  override fun onGoToSelectedPageClick() {
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
        .autoDisposable(event(FragmentLifecycle.STOP))
        .subscribe { presenter.loadForum(currentForumId) }
  }
}
