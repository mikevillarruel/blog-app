package com.example.blogapp.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogapp.core.Result
import com.example.blogapp.domain.camera.CameraRepo
import kotlinx.coroutines.Dispatchers

class CameraViewModel(private val cameraRepo: CameraRepo) : ViewModel() {

    fun uploadPhoto(bitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(cameraRepo.uploadPhoto(bitmap, description)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}

class CameraViewModelFactory(private val cameraRepo: CameraRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CameraViewModel(cameraRepo) as T
    }
}