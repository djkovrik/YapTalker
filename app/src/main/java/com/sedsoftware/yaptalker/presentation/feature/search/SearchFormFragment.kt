package com.sedsoftware.yaptalker.presentation.feature.search

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.search.options.SearchConditions
import com.sedsoftware.yaptalker.presentation.feature.search.options.SortingMode
import com.sedsoftware.yaptalker.presentation.feature.search.options.TargetPeriod
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_site_search.*
import java.util.ArrayList
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_site_search)
class SearchFormFragment : BaseFragment(), SearchFormView {

  companion object {
    fun getNewInstance() = SearchFormFragment()

    private const val SEARCH_IN = "titles"
    private const val CATEGORY_ALL = "all"
    private const val ALL_CATEGORIES_SIZE = 3
    private const val CATEGORY_FEED = "c_2"
    private const val CATEGORY_COMMUNICATION = "c_5"
    private const val CATEGORY_CHAOS = "c_3"
  }

  private var currentSearchPeriod = TargetPeriod.ALL_TIME
  private var currentSearchConditions = SearchConditions.ANY_WORD
  private var currentSortingMode = SortingMode.DATE

  @Inject
  @InjectPresenter
  lateinit var presenter: SearchFormPresenter

  @ProvidePresenter
  fun provideSearchFormPresenter() = presenter

  @Suppress("MagicNumber")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    RxAdapterView
      .itemSelections(search_period_spinner)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { itemId: Int? ->
        currentSearchPeriod = when (itemId) {
          1 -> TargetPeriod.TODAY
          2 -> TargetPeriod.DAYS_7
          3 -> TargetPeriod.DAYS_30
          4 -> TargetPeriod.DAYS_60
          5 -> TargetPeriod.DAYS_90
          6 -> TargetPeriod.DAYS_180
          7 -> TargetPeriod.DAYS_365
          else -> TargetPeriod.ALL_TIME
        }
      }

    RxRadioGroup
      .checkedChanges(search_group_conditions)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { itemId: Int? ->
        currentSearchConditions = when (itemId) {
          R.id.search_rb_words_all -> SearchConditions.ALL_WORDS
          R.id.search_rb_words_phrase -> SearchConditions.EXACT_PHRASE
          else -> SearchConditions.ANY_WORD
        }
      }

    RxRadioGroup
      .checkedChanges(search_group_sorting)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { itemId: Int? ->
        currentSortingMode = when (itemId) {
          R.id.search_rb_relevant -> SortingMode.DATE
          else -> SortingMode.RELEVANT
        }
      }

    RxView
      .clicks(search_button)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { prepareSearchRequest() }
  }

  override fun updateCurrentUiState() {
    context?.string(R.string.nav_drawer_search)?.let { presenter.setAppbarTitle(it) }
    presenter.setNavDrawerItem(NavigationSection.SITE_SEARCH)
  }

  private fun getCheckedCategories(): List<String> {

    val categories: MutableList<String> = ArrayList()

    if (search_box_feed.isChecked) {
      categories.add(CATEGORY_FEED)
    }

    if (search_box_communication.isChecked) {
      categories.add(CATEGORY_COMMUNICATION)
    }

    if (search_box_chaos.isChecked) {
      categories.add(CATEGORY_CHAOS)
    }

    if (categories.isEmpty() || categories.size == ALL_CATEGORIES_SIZE) {
      return listOf(CATEGORY_ALL)
    }

    return categories
  }

  private fun prepareSearchRequest() {

    val searchText = search_target_text.text.toString()

    if (searchText.isEmpty()) {
      return
    }

    val request = SearchRequest(
      searchFor = searchText,
      targetForums = getCheckedCategories(),
      searchIn = SEARCH_IN,
      searchHow = currentSearchConditions,
      sortBy = currentSortingMode,
      periodInDays = currentSearchPeriod
    )

    presenter.performSearchRequest(request)
  }
}
