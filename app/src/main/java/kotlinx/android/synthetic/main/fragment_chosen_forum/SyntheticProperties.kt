package kotlinx.android.synthetic.main.fragment_chosen_forum

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.forum_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.forum_refresh_layout)

val Fragment.forum_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.forum_refresh_layout)

val View.forum_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.forum_refresh_layout)

val Activity.forum_topics_list: RecyclerView
    get() = syntheticView(R.id.forum_topics_list)

val Fragment.forum_topics_list: RecyclerView
    get() = syntheticView(R.id.forum_topics_list)

val View.forum_topics_list: RecyclerView
    get() = syntheticView(R.id.forum_topics_list)
