package com.example.practice_p2papp.extensions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private val auth: FirebaseAuth =
	Firebase.auth


internal fun FirebaseAuth.authCheck() {
	if (auth.currentUser == null) {
		return
	}
}
