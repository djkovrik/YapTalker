package kotlinx.android.synthetic.main.fragment_news

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

val Activity.refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.refresh_layout)

val Fragment.refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.refresh_layout)

val View.refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.refresh_layout)

val Activity.news_list: RecyclerView
    get() = syntheticView(R.id.news_list)

val Fragment.news_list: RecyclerView
    get() = syntheticView(R.id.news_list)

val View.news_list: RecyclerView
    get() = syntheticView(R.id.news_list)

val Activity.news_fab: FloatingActionButton
    get() = syntheticView(R.id.news_fab)

val Fragment.news_fab: FloatingActionButton
    get() = syntheticView(R.id.news_fab)

val View.news_fab: FloatingActionButton
    get() = syntheticView(R.id.news_fab)
