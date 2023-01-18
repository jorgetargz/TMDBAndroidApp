package org.jorgetargz.movies.framework.list_trending_movies

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.ItemMediaBinding
import org.jorgetargz.movies.domain.models.Movie
import org.jorgetargz.movies.framework.utils.inflate
import org.jorgetargz.movies.framework.utils.loadUrl

class MoviesAdapter(private val listTrendingMoviesActions: ListTrendingMoviesActions) :
    ListAdapter<Movie, MoviesAdapter.ItemViewholder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            listTrendingMoviesActions,
            parent.inflate(R.layout.item_media),
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        private val listTrendingMoviesActions: ListTrendingMoviesActions,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemMediaBinding.bind(itemView)

        fun bind(item: Movie) = with(binding) {
            tvTitle.text = item.title
            item.posterPath?.let { ivMedia.loadUrl(it) }
                ?: ivMedia.setImageResource(R.drawable.ic_movie)

            card.setOnClickListener {
                listTrendingMoviesActions.onMovieClicked(item.title)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
