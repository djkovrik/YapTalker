package kotlinx.android.synthetic.main.fragment_forums_list

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.forums_list_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.forums_list_refresh_layout)

val Fragment.forums_list_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.forums_list_refresh_layout)

val View.forums_list_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.forums_list_refresh_layout)

val Activity.forums_list: RecyclerView
    get() = syntheticView(R.id.forums_list)

val Fragment.forums_list: RecyclerView
    get() = syntheticView(R.id.forums_list)

val View.forums_list: RecyclerView
    get() = syntheticView(R.id.forums_list)
