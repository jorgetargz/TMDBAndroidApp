package org.jorgetargz.movies.framework.list_trending_persons

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.ItemPersonBinding
import org.jorgetargz.movies.domain.models.Person
import org.jorgetargz.movies.framework.utils.inflate
import org.jorgetargz.movies.framework.utils.loadUrlFromTMDB
import org.jorgetargz.movies.framework.utils.loadUrlFromTMDBW342Size

class PersonsAdapter(
    private val listTrendingPersonsActions: ListTrendingPersonsActions,
    private val context: Context?
) : ListAdapter<Person, PersonsAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            listTrendingPersonsActions,
            context,
            parent.inflate(R.layout.item_person),
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        private val listTrendingPersonsActions: ListTrendingPersonsActions,
        private val context: Context?,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPersonBinding.bind(itemView)

        fun bind(item: Person) = with(binding) {
            tvName.text = item.name
            item.profilePath?.let { ivProfile.loadUrlFromTMDB(it) }
                ?: ivProfile.setImageResource(R.drawable.ic_movie)

            card.setOnClickListener {
                listTrendingPersonsActions.onPersonClicked(item.id)
            }

            while (
                knowForContainer.children.count() > 0
            ) {
                knowForContainer.removeViewAt(0)
            }

            item.knownFor.forEach {
                it.posterPath?.let { it1 ->
                    run {
                        val imageView = ImageView(context)
                        imageView.loadUrlFromTMDBW342Size(it1)
                        knowForContainer.addView(imageView)
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}
