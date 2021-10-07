package com.example.practice_p2papp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_p2papp.databinding.ItemArticleBinding
import com.example.practice_p2papp.extensions.loadThumbnailImage
import com.example.practice_p2papp.item.ArticleListItem
import java.text.SimpleDateFormat
import java.util.*

class ArticleListAdapter(val onClickListener: (ArticleListItem) -> Unit) : ListAdapter<ArticleListItem, ArticleListAdapter.ArticleViewHolder>(diffUtil) {

	inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
		RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SimpleDateFormat")
		fun bindView(articleListItem: ArticleListItem) = with(binding) {
			titleTextView.text = articleListItem.title
			priceTextView.text = articleListItem.price

			val format = SimpleDateFormat("MM월 dd일 HH시")
			val date = Date(articleListItem.date)
			dateTextView.text = format.format(date).toString()

			if(articleListItem.imageUriList.isNotEmpty()){
				thumbnailImageView.loadThumbnailImage(articleListItem.imageUriList,8f)
			}


			this.root.setOnClickListener {
				onClickListener(articleListItem)
			}
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
		ArticleViewHolder(
			ItemArticleBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
		holder.bindView(currentList[position])


	companion object {
		private val diffUtil = object : DiffUtil.ItemCallback<ArticleListItem>() {
			override fun areItemsTheSame(
				oldItem: ArticleListItem,
				newItem: ArticleListItem
			) =
				oldItem == newItem


			override fun areContentsTheSame(
				oldItem: ArticleListItem,
				newItem: ArticleListItem
			) =
				oldItem.date == newItem.date

		}
	}

}