package com.example.blogapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val REQUEST_IMAGE_CAPTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTakePicture = findViewById<Button>(R.id.btnTakePicture)
        imageView = findViewById(R.id.imageView)

        btnTakePicture.setOnClickListener {
            dispatchTakePictureIntent()
        }

        /*val db = FirebaseFirestore.getInstance()

        // Get data
        db.collection("cities").document("LA").addSnapshotListener { snapshot, error ->
            snapshot?.let { document ->
                Log.d("Firebase", "DocumentSnapshot data: ${document.data}")
                val city = document.toObject(City::class.java)
                val color = document.getString("color")
                val population = document.getLong("population")
                Log.d("Firebase", "Population: $population / Color: $color")
            }
        }

        // Set data
        db.collection("cities").document("NY").set(City(3000, "red")).addOnSuccessListener {
            Log.d("Firebase", "City saved")
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.toString())
        }*/

    }

    private fun dispatchTakePictureIntent() {

        throw RuntimeException("Crashlytics Test")

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Camera not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmpap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmpap)
            uploadPicture(imageBitmpap)
        }
    }

    private fun uploadPicture(bitmap: Bitmap) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { exception ->
                    throw exception
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()

                FirebaseFirestore.getInstance().collection("cities").document("LA")
                    .update(mapOf("imageUrl" to downloadUrl))

                Log.d("Storage", "uploadPicture: $downloadUrl")
            }
        }
    }
}

data class City(val population: Int = 0, val color: String = "", val imageUrl: String)