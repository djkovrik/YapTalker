package kotlinx.android.synthetic.main.fragment_bookmarks

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.bookmarks_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.bookmarks_refresh_layout)

val Fragment.bookmarks_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.bookmarks_refresh_layout)

val View.bookmarks_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.bookmarks_refresh_layout)

val Activity.bookmarks_list: RecyclerView
    get() = syntheticView(R.id.bookmarks_list)

val Fragment.bookmarks_list: RecyclerView
    get() = syntheticView(R.id.bookmarks_list)

val View.bookmarks_list: RecyclerView
    get() = syntheticView(R.id.bookmarks_list)
