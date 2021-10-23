package com.example.project_brooklyn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_brooklyn.databinding.ItemSearchResultBinding
import com.example.project_brooklyn.item.retrofitmodel.SearchResultItem


class SearchResultAdapter(private val searchOnClickLister : (SearchResultItem) -> Unit) :
	ListAdapter<SearchResultItem, SearchResultAdapter.SearchResultViewHolder>(diffUtil) {

	companion object {
		val diffUtil = object : DiffUtil.ItemCallback<SearchResultItem>() {
			override fun areItemsTheSame(
				oldItem: SearchResultItem,
				newItem: SearchResultItem
			): Boolean = false

			override fun areContentsTheSame(
				oldItem: SearchResultItem,
				newItem: SearchResultItem
			): Boolean = false

		}
	}

	inner class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
		RecyclerView.ViewHolder(binding.root) {

			fun bindViews(searchResultItem: SearchResultItem) = with(binding){
				mainTitleTextView.text = searchResultItem.locationName
				subTitleTextView.text = searchResultItem.fullAddress

				container.setOnClickListener {
					searchOnClickLister(searchResultItem)
				}
			}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
		SearchResultViewHolder(
			ItemSearchResultBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
		holder.bindViews(currentList[position])
	}


}