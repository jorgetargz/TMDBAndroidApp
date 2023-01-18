package org.jorgetargz.movies.framework.list_trending_tv_shows

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.ItemMediaBinding
import org.jorgetargz.movies.domain.models.TVShow
import org.jorgetargz.movies.framework.utils.inflate
import org.jorgetargz.movies.framework.utils.loadUrlFromTMDB

class TVShowsAdapter(private val listTrendingTVShowsActions: ListTrendingTVShowsActions) :
    ListAdapter<TVShow, TVShowsAdapter.ItemViewholder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            listTrendingTVShowsActions,
            parent.inflate(R.layout.item_media),
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        private val listTrendingTVShowsActions: ListTrendingTVShowsActions,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemMediaBinding.bind(itemView)

        fun bind(item: TVShow) = with(binding) {
            tvTitle.text = item.name
            item.posterPath?.let { ivMedia.loadUrlFromTMDB(it) } ?: ivMedia.setImageResource(R.drawable.ic_movie)

            card.setOnClickListener {
                listTrendingTVShowsActions.onTVShowClicked(item.id)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TVShow>() {
        override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
            return oldItem == newItem
        }
    }
}
