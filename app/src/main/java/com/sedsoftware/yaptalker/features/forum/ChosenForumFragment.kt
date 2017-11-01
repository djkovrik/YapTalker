package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.parsing.ForumPage
import com.sedsoftware.yaptalker.features.forum.adapter.ChosenForumAdapter
import com.sedsoftware.yaptalker.features.forum.adapter.ForumNavigationClickListener
import com.sedsoftware.yaptalker.features.forum.adapter.TopicItemClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_chosen_forum.*
import java.util.Locale

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

  @InjectPresenter
  lateinit var forumPresenter: ChosenForumPresenter

  override val layoutId: Int
    get() = R.layout.fragment_chosen_forum

  private val currentForumId: Int by lazy {
    arguments.getInt(FORUM_ID_KEY)
  }

  private lateinit var forumAdapter: ChosenForumAdapter

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    forumAdapter = ChosenForumAdapter(this, this)

    forumAdapter.setHasStableIds(true)

    forum_refresh_layout.setIndicatorColorScheme()

    with(forum_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = forumAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }

    forumPresenter.checkSavedState(currentForumId, savedInstanceState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    val panel = forumAdapter.getNavigationPanel()
    val topics = forumAdapter.getTopics()

    if (panel.isNotEmpty() && topics.isNotEmpty()) {
      forumPresenter.saveCurrentState(outState, currentForumId, panel.first(), topics)
    }
  }

  override fun subscribeViews() {
    RxSwipeRefreshLayout
        .refreshes(forum_refresh_layout)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { forumPresenter.loadForum(currentForumId) }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator(shouldShow: Boolean) {
    forum_refresh_layout.isRefreshing = shouldShow
  }

  override fun displayForumPage(page: ForumPage) {
    forumAdapter.refreshForumPage(page)
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
    forumPresenter.navigateToChosenTopic(Pair(currentForumId, topicId))
  }

  override fun onGoToFirstPageClick() {
    forumPresenter.goToFirstPage()
  }

  override fun onGoToLastPageClick() {
    forumPresenter.goToLastPage()
  }

  override fun onGoToPreviousPageClick() {
    forumPresenter.goToPreviousPage()
  }

  override fun onGoToNextPageClick() {
    forumPresenter.goToNextPage()
  }

  override fun onGoToSelectedPageClick() {
    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.navigation_go_to_page_title)
          .inputType(InputType.TYPE_CLASS_NUMBER)
          .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
            forumPresenter.goToChosenPage(input.toString().toInt())
          })
          .show()
    }
  }
}
