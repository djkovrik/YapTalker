package com.sedsoftware.yaptalker.features.topic.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.inflate
import com.sedsoftware.yaptalker.data.model.TopicNavigationPanel
import kotlinx.android.synthetic.main.controller_item_navigation_panel.view.*
import java.util.Locale

class TopicNavigationDelegateAdapter(val navigationClick: TopicNavigationClickListener) : ViewTypeDelegateAdapter {

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return NavigationViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ViewType) {
    holder as NavigationViewHolder
    holder.bindTo(item as TopicNavigationPanel)
  }

  inner class NavigationViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.controller_item_navigation_panel)) {

    private val pagesLabelTemplate = parent.context.getString(R.string.navigation_pages_template)

    fun bindTo(navigation: TopicNavigationPanel) {

      val currentPage = navigation.currentPage.toInt()
      val totalPages = navigation.totalPages.toInt()

      with(itemView) {
        navigation_pages_label.text = String.format(Locale.getDefault(), pagesLabelTemplate, currentPage, totalPages)

        navigation_go_first.isEnabled = (currentPage != 1)
        navigation_go_previous.isEnabled = (currentPage != 1)
        navigation_go_next.isEnabled = (currentPage < totalPages)
        navigation_go_last.isEnabled = (currentPage != totalPages)

        navigation_pages_label.setOnClickListener { navigationClick.onGoToSelectedPageClick() }
        navigation_go_first.setOnClickListener { navigationClick.onGoToFirstPageClick() }
        navigation_go_previous.setOnClickListener { navigationClick.onGoToPreviousPageClick() }
        navigation_go_next.setOnClickListener { navigationClick.onGoToNextPageClick() }
        navigation_go_last.setOnClickListener { navigationClick.onGoToLastPageClick() }
      }
    }
  }
}
