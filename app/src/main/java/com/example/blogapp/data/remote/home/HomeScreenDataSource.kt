package com.example.blogapp.data.remote.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {
    suspend fun getLatestPosts(): Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()

        querySnapshot.documents.forEach { post ->
            post.toObject(Post::class.java)?.let { newPost ->
                postList.add(newPost)
            }
        }

        return Resource.Success(postList)
    }
}