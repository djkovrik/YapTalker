package com.sedsoftware.yaptalker.presentation.features.activetopics

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
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.extensions.setIndicatorColorScheme
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastWarning
import com.sedsoftware.yaptalker.presentation.features.activetopics.adapters.ActiveTopicsAdapter
import com.sedsoftware.yaptalker.presentation.features.activetopics.adapters.ActiveTopicsItemClickListener
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_active_topics.*
import java.util.Locale
import javax.inject.Inject

// TODO () Refactor layouts to display full topic name
@LayoutResource(value = R.layout.fragment_active_topics)
class ActiveTopicsFragment : BaseFragment(), ActiveTopicsView, ActiveTopicsItemClickListener,
  NavigationPanelClickListener {

  companion object {
    fun getNewInstance() = ActiveTopicsFragment()
  }

  @Inject
  lateinit var topicsAdapter: ActiveTopicsAdapter

  @Inject
  lateinit var settings: Settings

  @Inject
  @InjectPresenter
  lateinit var presenter: ActiveTopicsPresenter

  @ProvidePresenter
  fun provideActiveTopicsPresenter() = presenter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(active_topics_list) {
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
      adapter = topicsAdapter
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      setHasFixedSize(true)
    }

    active_topics_refresh_layout.setIndicatorColorScheme()

    subscribeViews()
  }

  override fun showLoadingIndicator() {
    active_topics_refresh_layout.isRefreshing = true
  }

  override fun hideLoadingIndicator() {
    active_topics_refresh_layout.isRefreshing = false
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun appendActiveTopicItem(topic: YapEntity) {
    topicsAdapter.addActiveTopicItem(topic)
  }

  override fun clearActiveTopicsList() {
    topicsAdapter.clearActiveTopics()
  }

  override fun updateCurrentUiState() {
    context?.stringRes(R.string.nav_drawer_active_topics)?.let { presenter.setAppbarTitle(it) }
    presenter.setNavDrawerItem(NavigationSection.ACTIVE_TOPICS)
  }

  override fun scrollToViewTop() {
    active_topics_list?.layoutManager?.scrollToPosition(0)
  }

  override fun showCantLoadPageMessage(page: Int) {
    context?.stringRes(R.string.navigation_page_not_available)?.let { template ->
      toastWarning(String.format(Locale.getDefault(), template, page))
    }
  }

  override fun onActiveTopicItemClick(triple: Triple<Int, Int, Int>) {
    presenter.navigateToChosenTopic(triple)
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
      .refreshes(active_topics_refresh_layout)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { presenter.refreshActiveTopicsList() }
  }
}
