package com.example.practice_p2papp.articlelist

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practice_p2papp.FirebaseKey.Companion.DB_ARTICLES
import com.example.practice_p2papp.FirebaseKey.Companion.DB_USER_INFO
import com.example.practice_p2papp.databinding.ActivityAddArticleBinding
import com.example.practice_p2papp.item.ArticleListItem
import com.example.practice_p2papp.item.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddArticleActivity : AppCompatActivity() {

	private val imageUriList = mutableListOf<Uri>()

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}
	private val articleDB: DatabaseReference by lazy {
		Firebase.database.reference
	}

	private val userDB: DatabaseReference by lazy {
		Firebase.database.reference.child(DB_USER_INFO)
	}

	private var userNickName : String = ""


	private lateinit var binding: ActivityAddArticleBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAddArticleBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// userDB에서 nickName 가져온다.
		userDB.addChildEventListener(object : ChildEventListener{
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(UserItem::class.java)
				model ?: return

				userNickName = model.nickName
			}
			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onChildRemoved(snapshot: DataSnapshot) {}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}

		})

		setImageAddButtonListener()
		setSubmitButtonListener()
	}

	// 이미지 업로드
	// TODO 3. xml RecylclerView로 변경
	private fun setImageAddButtonListener() {

	}

	// TODO 2. 아이템 등록 버튼. 아직 이미지 관련된 구현 1도 안함. NickName도 안불러와짐...
	// DB에 항목 업로드, userId, nickName, title, content, price, date, imageUriList
	private fun setSubmitButtonListener() = with(binding) {
		submitButton.setOnClickListener {
			if (auth.currentUser == null) {
				return@setOnClickListener
			}

			val userId = auth.currentUser?.uid.toString()
			val nickName = userNickName
			val title = titleEditText.text.toString()
			val content = contentEditText.text.toString()
			val price = priceEditText.text.toString()
			val date = System.currentTimeMillis()

			// imageUrlList가 있을 떄와 없을떄로 나누어짐
			if (imageUriList.isNotEmpty()) {
				uploadArticles(userId, nickName, title, content, "$price 원", date, listOf())
			} else {
				uploadArticles(userId, nickName, title, content, "$price 원", date, listOf())

			}

		}

	}

	private fun uploadPhoto() {

	}

	private fun uploadArticles(
		userId: String,
		nickName: String,
		title: String,
		content: String,
		price: String,
		date:Long,
		imageUriList: List<Uri>
	) {
		val model = ArticleListItem(userId,nickName,title,content,price,date,imageUriList)

		articleDB.child(DB_ARTICLES).push().setValue(model)

		finish()

	}


}