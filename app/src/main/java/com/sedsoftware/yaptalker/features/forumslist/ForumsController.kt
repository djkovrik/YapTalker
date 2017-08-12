package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import android.view.View
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BaseController

class ForumsController: BaseController(), ForumsView {

  override fun getLayoutId() = R.layout.controller_forums_list

  override fun onViewBound(view: View, savedViewState: Bundle?) {
  }

  override fun showForums(forums: List<ForumItem>) {
  }

  override fun showErrorMessage(message: String) {
  }
}