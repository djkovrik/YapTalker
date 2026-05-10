package kotlinx.android.synthetic.main.item_navigation_panel.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView

val View.navigation_panel: CardView
    get() = syntheticView(R.id.navigation_panel)

val View.navigation_go_first: Button
    get() = syntheticView(R.id.navigation_go_first)

val View.navigation_go_previous: Button
    get() = syntheticView(R.id.navigation_go_previous)

val View.navigation_pages_label: TextView
    get() = syntheticView(R.id.navigation_pages_label)

val View.navigation_go_next: Button
    get() = syntheticView(R.id.navigation_go_next)

val View.navigation_go_last: Button
    get() = syntheticView(R.id.navigation_go_last)
