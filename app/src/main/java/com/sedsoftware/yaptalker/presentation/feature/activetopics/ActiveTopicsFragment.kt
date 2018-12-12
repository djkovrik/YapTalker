package com.sedsoftware.yaptalker.presentation.feature.activetopics

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
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
import com.sedsoftware.yaptalker.presentation.feature.activetopics.adapter.ActiveTopicsAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_active_topics.active_topics_list
import kotlinx.android.synthetic.main.fragment_active_topics.active_topics_refresh_layout
import java.util.Locale
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_active_topics)
class ActiveTopicsFragment : BaseFragment(), ActiveTopicsView {

    companion object {
        fun getNewInstance(): ActiveTopicsFragment = ActiveTopicsFragment()
    }

    @Inject
    lateinit var topicsAdapter: ActiveTopicsAdapter

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
        messagesDelegate.showMessageError(message)
    }

    override fun updateCurrentUiState() {
        setCurrentAppbarTitle(string(R.string.nav_drawer_active_topics))
        setCurrentNavDrawerItem(NavigationSection.ACTIVE_TOPICS)
    }

    override fun appendActiveTopicItem(item: DisplayedItemModel) {
        topicsAdapter.addActiveTopicItem(item)
    }

    override fun clearActiveTopicsList() {
        topicsAdapter.clearActiveTopics()
    }

    override fun scrollToViewTop() {
        active_topics_list?.layoutManager?.scrollToPosition(0)
    }

    override fun showPageSelectionDialog() {
        context?.let { ctx ->
            MaterialDialog.Builder(ctx)
                .title(R.string.navigation_go_to_page_title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(R.string.navigation_go_to_page_hint, 0, false) { _, input ->
                    presenter.goToChosenPage(input.toString().toInt())
                }
                .show()
        }
    }

    override fun showCantLoadPageMessage(page: Int) {
        messagesDelegate.showMessageWarning(
            String.format(Locale.getDefault(), string(R.string.navigation_page_not_available), page)
        )
    }

    private fun subscribeViews() {
        RxSwipeRefreshLayout.refreshes(active_topics_refresh_layout)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                presenter.refreshActiveTopicsList()
            }, { throwable: Throwable ->
                throwable.message?.let { showErrorMessage(it) }
            })
    }
}
