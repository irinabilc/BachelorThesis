package irinabilc.bachelorthesis.storage.textentrylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import irinabilc.bachelorthesis.ItemTextEntryBinding
import irinabilc.bachelorthesis.R
import irinabilc.bachelorthesis.model.TextEntry

class TextEntryListAdapter(private val clickListener: (TextEntry) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<TextEntry, TextEntryListAdapter.TextEntryViewHolder>(
        TextEntryDIffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextEntryViewHolder =
        TextEntryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_text_entry,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TextEntryViewHolder, position: Int) =
        holder.bind(getItem(position), clickListener)

    class TextEntryViewHolder(private val binding: ItemTextEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: TextEntry, clickListener: (TextEntry) -> Unit) {
            binding.textEntry = entry
            binding.root.setOnClickListener {
                clickListener(entry)
                true
            }
        }
    }

    class TextEntryDIffCallback : DiffUtil.ItemCallback<TextEntry>() {
        override fun areItemsTheSame(oldItem: TextEntry, newItem: TextEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TextEntry, newItem: TextEntry): Boolean =
            oldItem == newItem
    }
}