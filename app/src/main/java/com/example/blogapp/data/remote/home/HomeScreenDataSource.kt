package com.example.blogapp.data.remote.home

import com.example.blogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {
    suspend fun getLatestPosts(): List<Post> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING).get().await()

        querySnapshot.documents.forEach { post ->
            post.toObject(Post::class.java)?.let { newPost ->
                newPost.apply {
                    createdAt = post.getTimestamp(
                        "createdAt",
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )?.toDate()
                }
                postList.add(newPost)
            }
        }

        return postList /* .sortedByDescending { it.createdAt } */
    }
}