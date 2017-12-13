package com.sedsoftware.yaptalker.presentation.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.GetNewsList
import com.sedsoftware.yaptalker.domain.interactor.GetNewsList.Params
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.presentation.mappers.NewsModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import io.reactivex.observers.DisposableObserver
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class NewsPresenter @Inject constructor(
    private val router: Router,
    private val getNewsListUseCase: GetNewsList,
    private val newsModelMapper: NewsModelMapper
) : BasePresenter<NewsView>() {

  companion object {
    private const val NEWS_PER_PAGE = 50
  }

  private var currentPage = 0
  private var backToFirstPage = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadNews(loadFromFirstPage = true)
  }

  override fun onDestroy() {
    super.onDestroy()
    getNewsListUseCase.dispose()
  }

  fun handleFabVisibility(diff: Int) {
    when {
      diff > 0 -> viewState.hideFab()
      diff < 0 -> viewState.showFab()
    }
  }

  fun loadNews(loadFromFirstPage: Boolean) {

    backToFirstPage = loadFromFirstPage

    if (backToFirstPage) {
      currentPage = 0
    } else {
      currentPage += NEWS_PER_PAGE
    }

    loadDataForCurrentPage()
  }

  private fun loadDataForCurrentPage() {
    getNewsListUseCase.execute(getNewsObserver(), Params(pageNumber = currentPage))
  }

  private fun getNewsObserver() =
      object : DisposableObserver<BaseEntity>() {
        override fun onComplete() {

        }

        override fun onNext(item: BaseEntity) {
          if (backToFirstPage) {
            viewState.clearNewsList()
            backToFirstPage = false
          }

          displayLoadedNewsItem(item)
        }

        override fun onError(e: Throwable) {
          e.message?.let { viewState.showErrorMessage(it) }
        }
      }

  private fun displayLoadedNewsItem(item: BaseEntity) {
    newsModelMapper
        .transform(item)
        .also { entity: YapEntity -> viewState.appendNewsItem(entity) }
  }

  fun navigateToChosenTopic(triple: Triple<Int, Int, Int>) {
    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }
}
