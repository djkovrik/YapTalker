package com.sedsoftware.yaptalker.presentation.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sedsoftware.yaptalker.presentation.model.YapEntity

interface YapEntityDelegateAdapter {

  fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

  fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: YapEntity, position: Int = -1)
}
