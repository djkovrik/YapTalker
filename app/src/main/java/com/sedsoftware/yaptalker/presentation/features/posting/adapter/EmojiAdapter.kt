package com.sedsoftware.yaptalker.presentation.features.posting.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.EmojiModel
import kotlinx.android.synthetic.main.fragment_new_post_bottom_sheet_item.view.*
import timber.log.Timber
import java.util.ArrayList

class EmojiAdapter(
  private val clickListener: EmojiClickListener
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

  private var items: ArrayList<YapEntity> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {

    Timber.d("onCreateViewHolder")

    return EmojiViewHolder(parent)
  }

  override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
    Timber.d("onBindViewHolder")
    holder.bindTo(items[position])
  }

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getItemCount(): Int = items.size

  fun addEmojiItem(item: YapEntity) {
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

    fun bindTo(emoji: YapEntity) {

      emoji as EmojiModel

      Timber.d("Bind with ${emoji.link}")

      itemView.emoji_code.text = emoji.code
      itemView.emoji_image.loadFromUrl(emoji.link)
      itemView.emoji_container.setOnClickListener { clickListener.onEmojiClick(emoji.code) }
    }
  }
}
