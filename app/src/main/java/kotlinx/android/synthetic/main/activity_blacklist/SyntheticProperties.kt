package kotlinx.android.synthetic.main.activity_blacklist

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

val Activity.empty_label: TextView
    get() = syntheticView(R.id.empty_label)

val Fragment.empty_label: TextView
    get() = syntheticView(R.id.empty_label)

val View.empty_label: TextView
    get() = syntheticView(R.id.empty_label)

val Activity.blacklisted_topics: RecyclerView
    get() = syntheticView(R.id.blacklisted_topics)

val Fragment.blacklisted_topics: RecyclerView
    get() = syntheticView(R.id.blacklisted_topics)

val View.blacklisted_topics: RecyclerView
    get() = syntheticView(R.id.blacklisted_topics)
