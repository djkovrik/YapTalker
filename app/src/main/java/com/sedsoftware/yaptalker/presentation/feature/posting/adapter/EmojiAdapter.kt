package com.sedsoftware.yaptalker.presentation.feature.posting.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.EmojiModel
import java.util.ArrayList
import javax.inject.Inject

class EmojiAdapter @Inject constructor(
  private val clickListener: EmojiClickListener
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

  private var items: ArrayList<EmojiModel> = ArrayList()

  init {
    setHasStableIds(true)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EmojiViewHolder(parent)

  override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
    holder.bindTo(items[position])
  }

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getItemCount(): Int = items.size

  fun addEmojiItem(item: EmojiModel) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearEmojiList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }

  inner class EmojiViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_new_post_bottom_sheet_item)) {

    fun bindTo(emoji: DisplayedItemModel) {

      emoji as EmojiModel

      itemView.emoji_code.text = emoji.code
      itemView.emoji_image.loadFromUrl(emoji.link)
      itemView.emoji_container.setOnClickListener { clickListener.onEmojiClicked(emoji.code) }
    }
  }
}
