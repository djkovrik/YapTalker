package kotlinx.android.synthetic.main.fragment_new_post_bottom_sheet_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

val View.emoji_container: LinearLayout
    get() = syntheticView(R.id.emoji_container)

val View.emoji_code: TextView
    get() = syntheticView(R.id.emoji_code)

val View.emoji_image: ImageView
    get() = syntheticView(R.id.emoji_image)
