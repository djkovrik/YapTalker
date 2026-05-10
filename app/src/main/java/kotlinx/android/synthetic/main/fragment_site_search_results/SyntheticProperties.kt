package kotlinx.android.synthetic.main.fragment_site_search_results

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.search_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.search_refresh_layout)

val Fragment.search_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.search_refresh_layout)

val View.search_refresh_layout: SwipeRefreshLayout
    get() = syntheticView(R.id.search_refresh_layout)

val Activity.search_results_list: RecyclerView
    get() = syntheticView(R.id.search_results_list)

val Fragment.search_results_list: RecyclerView
    get() = syntheticView(R.id.search_results_list)

val View.search_results_list: RecyclerView
    get() = syntheticView(R.id.search_results_list)
