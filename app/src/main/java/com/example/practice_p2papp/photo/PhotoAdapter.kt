package com.example.practice_p2papp.photo

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_p2papp.databinding.ItemPhotoBinding
import com.example.practice_p2papp.extensions.loadCenterCrop

class PhotoAdapter(private val removePhotoListener: (Uri) -> Unit) :
	RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

	private var imageList : List<Uri> = listOf()

	inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bindImageView(data: Uri) = with(binding) {
			photoImageView.loadCenterCrop(data.toString(), 4f)

		}

		fun removePhoto(data: Uri) = with(binding) {
			photoImageView.setOnClickListener {
				removePhotoListener(data)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
		PhotoViewHolder(
			ItemPhotoBinding.inflate(
				LayoutInflater.from(parent.context)
			)
		)

	override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
		holder.bindImageView(imageList[position])
		holder.removePhoto(imageList[position])
	}


	override fun getItemCount(): Int =
		imageList.size

	@SuppressLint("NotifyDataSetChanged")
	fun setPhotoList(imageList : List<Uri>) {
	this.imageList = imageList
		notifyDataSetChanged()
	}


}