package com.example.blogapp.data.remote.home

import com.example.blogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HomeScreenDataSource {

    @ExperimentalCoroutinesApi
    suspend fun getLatestPosts(): Flow<List<Post>> = callbackFlow {
        val postList = mutableListOf<Post>()

        var postReference: Query? = null

        try {
            postReference = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
        } catch (e: Throwable) {
            close(e)
        }

        val subscription = postReference?.addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener

            try {
                postList.clear()
                value.documents.forEach { post ->
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
            } catch (e: Throwable) {
                close(e)
            }

            offer(postList)
        }

        awaitClose { subscription?.remove() }
        // return postList /* .sortedByDescending { it.createdAt } */
    }
}