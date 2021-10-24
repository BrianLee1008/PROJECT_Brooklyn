package com.example.project_brooklyn.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_brooklyn.databinding.ItemRecentSearchBinding
import com.example.project_brooklyn.item.room.entity.HistoryEntity

class RecentSearchAdapter(private val onClickListener : (String) -> Unit) :
	ListAdapter<HistoryEntity, RecentSearchAdapter.RecentSearchViewHolder>(diffUtil) {

	companion object {
		val diffUtil = object : DiffUtil.ItemCallback<HistoryEntity>() {
			override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean =
				false

			override fun areContentsTheSame(
				oldItem: HistoryEntity,
				newItem: HistoryEntity
			): Boolean = false
		}
	}

	inner class RecentSearchViewHolder(private val binding: ItemRecentSearchBinding) :
		RecyclerView.ViewHolder(binding.root) {
			fun bind(history: HistoryEntity) = with(binding){
				mainTitleTextView.text = history.keyword

				removeButton.setOnClickListener {
					onClickListener(history.keyword.toString())
				}

			}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder =
		RecentSearchViewHolder(
			ItemRecentSearchBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
		holder.bind(currentList[position])
	}

}
