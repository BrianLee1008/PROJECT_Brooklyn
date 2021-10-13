package com.example.practice_p2papp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_p2papp.databinding.ItemDetailArticleImageViewpagerBinding
import com.example.practice_p2papp.extensions.loadImage

// xo 이미지 리스트 받아와서 ViewPager
class DetailArticleViewPagerAdapter() :
	RecyclerView.Adapter<DetailArticleViewPagerAdapter.ViewPagerViewHolder>() {

	var imageUrlList = listOf<String>()

	inner class ViewPagerViewHolder(private val binding: ItemDetailArticleImageViewpagerBinding) :
		RecyclerView.ViewHolder(binding.root) {

			fun bindImage(imageUrl: String) = with(binding){
				imageView.loadImage(imageUrl)
			}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder =
		ViewPagerViewHolder(
			ItemDetailArticleImageViewpagerBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int)  =
		holder.bindImage(imageUrlList[position])

	override fun getItemCount(): Int =
		imageUrlList.size

	@SuppressLint("NotifyDataSetChanged")
	fun setImageViewPagerList(imageUrlList : List<String>){
		this.imageUrlList = imageUrlList
		notifyDataSetChanged()
	}
}