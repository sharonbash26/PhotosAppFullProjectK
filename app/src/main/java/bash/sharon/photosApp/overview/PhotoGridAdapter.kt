package bash.sharon.photosApp.overview

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bash.sharon.photosApp.R
import bash.sharon.photosApp.databinding.GridViewItemBinding
import bash.sharon.photosApp.network.HitsItem

class PhotoGridAdapter : ListAdapter<HitsItem, PhotoGridAdapter.PhotoViewHolder>(DiffCallback) {
    private val sharedPrefFile = "photoApp"
    private val id_key_sharedPer = "favorite_id_keys"


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoGridAdapter.PhotoViewHolder {
        return PhotoViewHolder(
            GridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.PhotoViewHolder, position: Int) {
        val hitItem = getItem(position)
        holder.bind(hitItem)
    }

    inner class PhotoViewHolder(private var binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hitItem: HitsItem) {
            val id: Int? = hitItem.id
            val sharedPreferences: SharedPreferences =
                binding.root.context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            binding.favoriteImage.setImageResource(
                if (sharedPreferences.getStringSet(id_key_sharedPer, mutableSetOf())
                        ?.contains(id.toString()) == true
                ) R.drawable.ic_favorite_full else R.drawable.ic_favorite_empty
            )

            binding.root.setOnClickListener {

                val list_ids =
                    sharedPreferences.getStringSet(id_key_sharedPer, mutableSetOf())
                        ?.toMutableList()
                // In this case - the photo is on favorites
                if (list_ids != null && list_ids.contains(id.toString())) {
                    list_ids.remove(id.toString())
                } else {
                    list_ids?.add(id.toString())
                }
                // The most important action -> Save our update set to shared preferences
                sharedPreferences.edit().putStringSet(id_key_sharedPer, list_ids?.toSet()).apply()
                notifyDataSetChanged()
            }
            binding.hitItem = hitItem
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<HitsItem>() {

        override fun areItemsTheSame(oldItem: HitsItem, newItem: HitsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HitsItem, newItem: HitsItem): Boolean {
            return oldItem.largeImageURL == newItem.largeImageURL
        }


    }


}


