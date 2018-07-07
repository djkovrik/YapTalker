package com.sedsoftware.yaptalker.presentation.base.navigation

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import kotlinx.android.synthetic.main.item_navigation_panel.view.*

class NavigationPanelDelegateAdapter(
    val navigationClick: NavigationPanelClickListener
) : YapEntityDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = NavigationViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
        holder as NavigationViewHolder
        holder.bindTo(item as NavigationPanelModel)
    }

    inner class NavigationViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_navigation_panel)) {

        fun bindTo(navigation: NavigationPanelModel) {
            with(itemView) {
                navigation_pages_label.text = navigation.navigationLabel
                navigation_go_first.isEnabled = (navigation.currentPage != 1)
                navigation_go_previous.isEnabled = (navigation.currentPage != 1)
                navigation_go_next.isEnabled = (navigation.currentPage < navigation.totalPages)
                navigation_go_last.isEnabled = (navigation.currentPage != navigation.totalPages)

                navigation_pages_label.setOnClickListener { navigationClick.goToSelectedPage() }
                navigation_go_first.setOnClickListener { navigationClick.goToFirstPage() }
                navigation_go_previous.setOnClickListener { navigationClick.goToPreviousPage() }
                navigation_go_next.setOnClickListener { navigationClick.goToNextPage() }
                navigation_go_last.setOnClickListener { navigationClick.goToLastPage() }
            }
        }
    }
}
