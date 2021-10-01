package com.example.blogapp.domain.camera

import android.graphics.Bitmap

interface CameraRepo {
    suspend fun uploadPhoto(bitmap: Bitmap, description: String)
}