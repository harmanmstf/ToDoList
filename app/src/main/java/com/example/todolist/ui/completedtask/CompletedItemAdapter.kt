package com.example.todolist.ui.completedtask


import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.Item
import com.example.todolist.databinding.ListItemBinding


class CompletedItemAdapter(private val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, CompletedItemAdapter.ItemViewHolder>(DiffCallback) {


    private val itemCountLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getItemCountLiveData(): LiveData<Int> {
        return itemCountLiveData}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    override fun submitList(list: List<Item>?) {
        super.submitList(list)
        itemCountLiveData.value = itemCount
    }

    class ItemViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            val crossedOutText = "<del>${item.itemTask}</del>"
            val formattedText: Spanned = HtmlCompat.fromHtml(crossedOutText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.itemTitle.text = formattedText
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemTask == newItem.itemTask
            }
        }
    }
}