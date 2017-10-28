package com.sedsoftware.yaptalker.features.forumslist

class ForumsController {
//    BaseController(), ForumsView {
//
//  companion object {
//    private const val FORUMS_LIST_KEY = "FORUMS_LIST"
//  }
//
//  @InjectPresenter
//  lateinit var forumsPresenter: ForumsPresenter
//
//  private lateinit var forumsAdapter: ForumsAdapter
//
//  override val controllerLayoutId: Int
//    get() = R.layout.controller_forums_list
//
//  override fun onViewBound(view: View, savedViewState: Bundle?) {
//
//    forumsAdapter = ForumsAdapter {
//      val bundle = Bundle()
//      bundle.putInt(ChosenForumController.FORUM_ID_KEY, it)
//      router.pushController(
//          RouterTransaction.with(ChosenForumController(bundle))
//              .pushChangeHandler(FadeChangeHandler())
//              .popChangeHandler(FadeChangeHandler()))
//    }
//
//    forumsAdapter.setHasStableIds(true)
//
//    view.forums_list_refresh_layout.setIndicatorColorScheme()
//
//    with(view.forums_list) {
//      val linearLayout = LinearLayoutManager(context)
//      layoutManager = linearLayout
//      adapter = forumsAdapter
//
//      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//      setHasFixedSize(true)
//    }
//
//    forumsPresenter.checkSavedState(savedViewState, FORUMS_LIST_KEY)
//    forumsPresenter.updateAppbarTitle(view.context.stringRes(R.string.nav_drawer_forums))
//  }
//
//  override fun subscribeViews(parent: View) {
//
//    parent.forums_list_refresh_layout?.let {
//      RxSwipeRefreshLayout
//          .refreshes(parent.forums_list_refresh_layout)
//          .autoDisposeWith(scopeProvider)
//          .subscribe { forumsPresenter.loadForumsList() }
//    }
//  }
//
//  override fun onSaveViewState(view: View, outState: Bundle) {
//    super.onSaveViewState(view, outState)
//    val forums = forumsAdapter.getForums()
//    if (forums.isNotEmpty()) {
//      outState.putParcelableArrayList(FORUMS_LIST_KEY, ArrayList(forums))
//    }
//  }
//
//  override fun onDestroyView(view: View) {
//    super.onDestroyView(view)
//    view.forums_list.adapter = null
//  }
//
//  override fun showErrorMessage(message: String) {
//    toastError(message)
//  }
//
//  override fun showLoadingIndicator(shouldShow: Boolean) {
//    view?.forums_list_refresh_layout?.isRefreshing = shouldShow
//  }
//
//  override fun clearForumsList() {
//    forumsAdapter.clearForumsList()
//  }
//
//  override fun appendForumItem(item: ForumItem) {
//    forumsAdapter.addForumsListItem(item)
//  }
//
//  override fun appendForumsList(list: List<ForumItem>) {
//    forumsAdapter.addForumsList(list)
//  }
}
