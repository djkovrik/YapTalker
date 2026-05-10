package kotlinx.android.synthetic.main.fragment_chosen_topic

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout

val Activity.topic_refresh_layout: SwipyRefreshLayout
    get() = syntheticView(R.id.topic_refresh_layout)

val Fragment.topic_refresh_layout: SwipyRefreshLayout
    get() = syntheticView(R.id.topic_refresh_layout)

val View.topic_refresh_layout: SwipyRefreshLayout
    get() = syntheticView(R.id.topic_refresh_layout)

val Activity.topic_posts_list: RecyclerView
    get() = syntheticView(R.id.topic_posts_list)

val Fragment.topic_posts_list: RecyclerView
    get() = syntheticView(R.id.topic_posts_list)

val View.topic_posts_list: RecyclerView
    get() = syntheticView(R.id.topic_posts_list)
