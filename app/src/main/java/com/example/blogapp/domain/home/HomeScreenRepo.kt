package com.example.blogapp.domain.home

import com.example.blogapp.data.model.Post

interface HomeScreenRepo {
    suspend fun getLatestPosts(): List<Post>
}