package com.sedsoftware.yaptalker.features.activetopics

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
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import com.sedsoftware.yaptalker.commons.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage
import com.sedsoftware.yaptalker.features.activetopics.adapter.ActiveTopicsAdapter
import com.sedsoftware.yaptalker.features.activetopics.adapter.ActiveTopicsItemClickListener
import com.sedsoftware.yaptalker.features.activetopics.adapter.ActiveTopicsNavigationClickListener
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_active_topics.*
import java.util.Locale

class ActiveTopicsFragment :
    BaseFragment(), ActiveTopicsView, ActiveTopicsItemClickListener, ActiveTopicsNavigationClickListener {

  companion object {
    fun getNewInstance() = ActiveTopicsFragment()
  }

  @InjectPresenter
  lateinit var activeTopicsPresenter: ActiveTopicsPresenter

  override val layoutId: Int
    get() = R.layout.fragment_active_topics

  private lateinit var activeTopicsAdapter: ActiveTopicsAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activeTopicsAdapter = ActiveTopicsAdapter(this, this)

    activeTopicsAdapter.setHasStableIds(true)

    active_topics_refresh_layout.setIndicatorColorScheme()

    with(active_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = activeTopicsAdapter

      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

      setHasFixedSize(true)
    }
  }

  override fun subscribeViews() {
    RxSwipeRefreshLayout
        .refreshes(active_topics_refresh_layout)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe { activeTopicsPresenter.refreshTopicsList() }
  }

  override fun updateAppbarTitle() {
    context?.stringRes(R.string.nav_drawer_active_topics)?.let { title ->
      activeTopicsPresenter.setAppbarTitle(title)
    }
  }

  override fun highlightCurrentNavDrawerItem() {
    activeTopicsPresenter.setNavDrawerItem(NavigationDrawerItems.ACTIVE_TOPICS)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLoadingIndicator() {
    active_topics_refresh_layout.isRefreshing = true
  }

  override fun hideLoadingIndicator() {
    active_topics_refresh_layout.isRefreshing = false
  }

  override fun displayActiveTopicsPage(page: ActiveTopicsPage) {
    activeTopicsAdapter.refreshActiveTopicsPage(page)
  }

  override fun scrollToViewTop() {
    active_topics_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showCantLoadPageMessage(page: Int) {
    context?.stringRes(R.string.navigation_page_not_available)?.let { template ->
      toastWarning(String.format(Locale.getDefault(), template, page))
    }
  }

  override fun onTopicClick(forumId: Int, topicId: Int) {
    activeTopicsPresenter.navigateToChosenTopic(Triple(forumId, topicId, 0))
  }

  override fun onGoToFirstPageClick() {
    activeTopicsPresenter.goToFirstPage()
  }

  override fun onGoToLastPageClick() {
    activeTopicsPresenter.goToLastPage()
  }

  override fun onGoToPreviousPageClick() {
    activeTopicsPresenter.goToPreviousPage()
  }

  override fun onGoToNextPageClick() {
    activeTopicsPresenter.goToNextPage()
  }

  override fun onGoToSelectedPageClick() {
    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.navigation_go_to_page_title)
          .inputType(InputType.TYPE_CLASS_NUMBER)
          .input(R.string.navigation_go_to_page_hint, 0, false, { _, input ->
            activeTopicsPresenter.goToChosenPage(input.toString().toInt())
          })
          .show()
    }
  }
}
