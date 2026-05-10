package kotlinx.android.synthetic.main.fragment_bookmarks_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

val View.bookmark_delete_icon: ImageView
    get() = syntheticView(R.id.bookmark_delete_icon)

val View.bookmark_title: TextView
    get() = syntheticView(R.id.bookmark_title)
