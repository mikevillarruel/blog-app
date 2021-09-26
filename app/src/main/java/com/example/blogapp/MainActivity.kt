package com.example.blogapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance()

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
        }

    }
}

data class City(val population: Int = 0, val color: String = "")