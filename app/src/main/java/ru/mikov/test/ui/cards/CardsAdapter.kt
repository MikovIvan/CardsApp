package ru.mikov.test.ui.cards

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_card.view.*
import ru.mikov.test.R
import ru.mikov.test.data.local.entities.Card


class CardsAdapter(
    private val listener: (Card) -> Unit,
    private val longListener: (Card) -> Unit,
) : PagedListAdapter<Card, CardsAdapter.CardsHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsHolder {
        val containerView = from(parent.context).inflate(
            R.layout.item_card,
            parent,
            false
        )
        return CardsHolder(containerView)
    }


    override fun onBindViewHolder(holder: CardsHolder, position: Int) {
        holder.bind(getItem(position), listener, longListener)
    }

    class CardsHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bind(
            item: Card?,
            listener: (Card) -> Unit,
            longListener: (Card) -> Unit
        ) {
            Glide.with(itemView)
                .load(item!!.texture.front)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(containerView.iv_card)
            this.itemView.setOnClickListener { listener.invoke(item) }
            this.itemView.setOnLongClickListener {
                longListener.invoke(item)
                true
            }
        }
    }


    class ArticleDiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
            oldItem.cardId == newItem.cardId

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
            oldItem == newItem
    }
}