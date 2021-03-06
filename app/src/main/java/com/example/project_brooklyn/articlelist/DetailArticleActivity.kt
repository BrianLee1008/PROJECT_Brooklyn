package com.example.project_brooklyn.articlelist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_brooklyn.adapter.DetailArticleViewPagerAdapter
import com.example.project_brooklyn.databinding.ActivityDetailArticleBinding
import com.example.project_brooklyn.extensions.circleCropImage
import com.example.project_brooklyn.item.ArticleListItem
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.DBRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DetailArticleActivity : AppCompatActivity() {


	private lateinit var viewPagerAdapter: DetailArticleViewPagerAdapter
	private val appRepository = DBRepository()
	private lateinit var getArticleInfo :ArticleListItem

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel>{ FirebaseViewModelFactory(appRepository) }
	private lateinit var binding: ActivityDetailArticleBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailArticleBinding.inflate(layoutInflater)
		setContentView(binding.root)

		if (firebaseDBViewModel.auth.currentUser == null) {
			return
		}

		intent?.let {
			getArticleInfo = it.getSerializableExtra(ARTICLE_INFO_KEY) as ArticleListItem
		}

		lifecycleScope.launch {
			initViews(
				nickName = getArticleInfo.nickName,
				title = getArticleInfo.title,
				content = getArticleInfo.content,
				price = getArticleInfo.price,
				date = getArticleInfo.date,
				userProfileImage = getArticleInfo.userProfileImage
			)
			initViewPager(getArticleInfo.imageUriList)
		}

		setStartChatButtonListener(
			// TODO buyerNickName ??? nickName?????? ??????
			sellerId = getArticleInfo.userId,
			buyerNickName = firebaseDBViewModel.auth.currentUser!!.uid,
			sellerNickName = getArticleInfo.nickName,
			buyerProfileImage = getArticleInfo.userProfileImage,
			currentTime = getArticleInfo.date,
			articleTitle = getArticleInfo.title
		)

	}


	@SuppressLint("SimpleDateFormat")
	private suspend fun initViews(
		nickName: String,
		title: String,
		content: String,
		price: String,
		date: Long,
		userProfileImage: String
	) = withContext(Dispatchers.IO) {
		viewPagerAdapter = DetailArticleViewPagerAdapter()

		val format = SimpleDateFormat("MM??? dd??? HH???")
		val currentDate = Date(date)


		lifecycleScope.launch {
			binding.let {
				it.sellerNickNameTextView.text = nickName
				it.sellerProfileImageView.circleCropImage(userProfileImage)
				it.articleTitleTextView.text = title
				it.articleDescriptionTextView.text = content
				it.articlePriceTextView.text = price
				it.articleCurrentTime.text = format.format(currentDate).toString()
			}
		}
	}

	private fun initViewPager(imageUriList: List<String>) = with(binding) {
		viewPagerAdapter = DetailArticleViewPagerAdapter()
		viewPager2ImageView.adapter = viewPagerAdapter

		viewPagerAdapter.setImageViewPagerList(imageUriList)

	}

	private fun setStartChatButtonListener(
		sellerId: String,
		buyerNickName: String,
		sellerNickName: String,
		buyerProfileImage: String,
		currentTime: Long,
		articleTitle: String
	) = with(binding) {

		chatButton.setOnClickListener {
			if (firebaseDBViewModel.auth.currentUser == null) {
				return@setOnClickListener
			}

			firebaseDBViewModel.uploadChatListInfoInDB(
				sellerId = sellerId,
				buyerNickName = buyerNickName,
				sellerNickName = sellerNickName,
				buyerProfileImage = buyerProfileImage,
				currentTime = currentTime,
				articleTitle = articleTitle
			)

			// DB ?????? ?????? - ???????????? ????????? ????????? ????????? ??? ????????? ????????? ?????? ???????????? ?????? ??????. ??????????????? ????????????. ????????? ???????????? - ?????? ??????????????? ????????? ????????? ?????? ?????? DB ??????
//			chatDB.child(DB_SELLER_CHAT).child(sellerId).child(articleTitle).setValue(chatRoomList)
//			chatDB.child(DB_BUYER_CHAT).child(buyerNickName)
//				.child(articleTitle).setValue(chatRoomList)

			Snackbar.make(binding.root, "???????????? ?????? ????????????! ?????? ????????? ??????????????????.", Snackbar.LENGTH_SHORT)
				.show()

		}

	}

	companion object{
		const val ARTICLE_INFO_KEY = "path"
	}


}