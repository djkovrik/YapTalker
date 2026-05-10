package kotlinx.android.synthetic.main.fragment_active_topics

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.active_topics_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.active_topics_refresh_layout)

val Fragment.active_topics_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.active_topics_refresh_layout)

val View.active_topics_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.active_topics_refresh_layout)

val Activity.active_topics_list: RecyclerView
    get() = syntheticView(R.id.active_topics_list)

val Fragment.active_topics_list: RecyclerView
    get() = syntheticView(R.id.active_topics_list)

val View.active_topics_list: RecyclerView
    get() = syntheticView(R.id.active_topics_list)
