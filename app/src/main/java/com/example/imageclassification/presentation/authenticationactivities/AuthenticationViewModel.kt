package com.example.imageclassification.presentation.authenticationactivities

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.example.imageclassification.bases.BaseViewModel
import com.example.imageclassification.data.local.UserSessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager
) : BaseViewModel() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var errMessage: MutableLiveData<String> = MutableLiveData()
    var user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    var name: MutableLiveData<String> = MutableLiveData()
    fun setupFirebaseAuth() {
        auth = Firebase.auth
        db = Firebase.firestore
    }

    fun tryLoginWithoutCredentials() {
        isLoading.postValue(true)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.let {
                db.collection("names").document(auth.currentUser?.email.toString()).get()
                    .addOnCompleteListener {
                        if (it.result["name"] != null) {
                            name.value = it.result["name"].toString()
                            user.value = auth.currentUser
                            isLoading.postValue(false)
                        } else {
                            isLoading.postValue(false)
                        }
                    }
            }
        } else {
            println("Not logged in")
            isLoading.postValue(false)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun login(email: String, password: String) {
        isLoading.postValue(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println("sign in:success")
                    db.collection("names").document(email).get()
                        .addOnCompleteListener {
                            if (it.result["name"] != null) {
                                name.value = (it.result["name"].toString())
                                user.value = (auth.currentUser)
                                isLoading.postValue(false)
                            }
                        }
                } else {
                    println("sign in:failure" + it.exception)
                    errMessage.postValue(it.exception?.message)
                    isLoading.postValue(false)
                }
            }
    }

    fun signup(email: String, password: String, fullName: String) {
        isLoading.postValue(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    db.collection("names").document(email).set(hashMapOf("name" to fullName))
                        .addOnCompleteListener {
                            name.postValue(fullName)
                            user.postValue(auth.currentUser)
                            isLoading.postValue(false)
                        }
                } else {
                    errMessage.postValue(it.exception?.message)
                    println("createUserWithEmail:failure" + it.exception)
                    isLoading.postValue(false)
                }
            }
    }
}