package com.sedsoftware.yaptalker.presentation.feature.search

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class SearchFormPresenter @Inject constructor(
    private val router: Router
) : BasePresenter<SearchFormView>() {

    override fun attachView(view: SearchFormView?) {
        super.attachView(view)
        viewState.updateCurrentUiState()
    }

    fun performSearchRequest(request: SearchRequest) {
        router.navigateTo(NavigationScreen.SEARCH_RESULTS, request)
    }
}
