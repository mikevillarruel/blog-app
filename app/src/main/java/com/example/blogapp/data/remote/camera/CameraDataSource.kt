package com.example.blogapp.data.remote.camera

import android.graphics.Bitmap
import com.example.blogapp.data.model.Post
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class CameraDataSource {
    suspend fun uploadPhoto(bitmap: Bitmap, description: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val randomName = UUID.randomUUID().toString()
        val imageRef =
            FirebaseStorage.getInstance().reference.child("${user?.uid}/posts/$randomName.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl =
            imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        user.let {
            FirebaseFirestore.getInstance().collection("posts").add(
                Post(
                    profileName = it?.displayName.toString(),
                    postDescription = description,
                    profilePicture = it?.photoUrl.toString(),
                    postTimestamp = Timestamp.now(),
                    postImage = downloadUrl,
                    uid = user?.uid.toString()
                )
            )
        }
    }
}