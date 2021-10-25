package com.example.project_brooklyn.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_brooklyn.databinding.ItemArticleBinding
import com.example.project_brooklyn.extensions.loadThumbnailImage
import com.example.project_brooklyn.item.ArticleListItem
import java.text.SimpleDateFormat
import java.util.*

class ArticleListAdapter(val onClickListener: (ArticleListItem) -> Unit) :
	ListAdapter<ArticleListItem, ArticleListAdapter.ArticleViewHolder>(diffUtil) {

	inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
		RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SimpleDateFormat")
		fun bindView(articleListItem: ArticleListItem) = with(binding) {
			titleTextView.text = articleListItem.title
			priceTextView.text = articleListItem.price

			val format = SimpleDateFormat("MM월 dd일 HH시")
			val date = Date(articleListItem.date)
			dateTextView.text = format.format(date).toString()

			// xo 이미지는 들어가는데 노출이 안되는 이 기이한 현상을 보라... 앱을 삭제했다 다시 깔면 처음 이미지 빼고 오류 뜨는 것으로 보임
			if (articleListItem.imageUriList.isNotEmpty()) {
				thumbnailImageView.loadThumbnailImage(articleListItem.imageUriList, 8f)
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
				oldItem.userId == newItem.userId


			override fun areContentsTheSame(
				oldItem: ArticleListItem,
				newItem: ArticleListItem
			) =
				oldItem.date == newItem.date

		}
	}

}