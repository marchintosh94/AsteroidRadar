package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.databinding.HeaderItemViewBinding
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class AsteroidsAdapter(private val onClickCallback: OnClickCallback): ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {
    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(HeaderItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_VIEW_TYPE_ITEM -> AsteroidViewHolder(AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }
    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder){
            is AsteroidViewHolder -> {
                val asteroid = item as DataItem.AsteroidItem
                holder.itemView.setOnClickListener {
                    onClickCallback.onClick(asteroid.asteroid)
                }
                holder.bind(asteroid.asteroid)
            }
            is HeaderViewHolder -> {
                val picture = item as DataItem.Header
                holder.bind(picture.pictureOfDay)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.AsteroidItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(pictureOfDay: PictureOfDay, asteroidList: List<Asteroid>?){
        CoroutineScope(Dispatchers.Default).launch {
            val items = when(asteroidList){
                null -> listOf(DataItem.Header(pictureOfDay))
                else -> listOf(DataItem.Header(pictureOfDay)) + asteroidList.map { DataItem.AsteroidItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    /**
     * The [AsteroidViewHolder] constructor takes the binding variable from the associated
     * AsteroidListItem, which nicely gives it access to the full [Asteroid] information.
     */
    class AsteroidViewHolder(private var binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (item: Asteroid){
            binding.item = item
            val context = binding.root.context
            binding.hazardIcon.contentDescription = if (item.isPotentiallyHazardous) context.getString(
                R.string.potentially_hazardous_asteroid_icon) else context.getString(R.string.not_hazardous_asteroid_icon)
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * The [HeaderViewHolder] constructor takes the binding variable from the associated
     * HeaderItemView, which nicely gives it access to the full [PictureOfDay] information.
     */
    class HeaderViewHolder(private var binding: HeaderItemViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (item: PictureOfDay){
            binding.pictureOfDay = item
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Determine which items has been updated in the [List] of [Asteroid]
     * Help [RecyclerView] to update item layout information
     */
    companion object DiffCallback: DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.
     * Passes the [Asteroid] associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Asteroid]
     */
    class OnClickCallback(val onClickCallback: (item: Asteroid) -> Unit){
        fun onClick(item: Asteroid) = onClickCallback(item)
    }

}

sealed class DataItem{
    abstract val id: Long

    data class AsteroidItem(val asteroid: Asteroid): DataItem(){
        override val id: Long = asteroid.id
    }

    data class Header(val pictureOfDay: PictureOfDay): DataItem(){
        override val id: Long = Long.MIN_VALUE
    }
}