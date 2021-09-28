package com.example.blogapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.blogapp.R
import com.example.blogapp.data.model.Post
import com.example.blogapp.databinding.FragmentHomeScreenBinding
import com.example.blogapp.ui.home.adapter.HomeScreenAdapter
import com.google.firebase.Timestamp

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        val postList = listOf<Post>(
            Post(
                "https://as2.ftcdn.net/v2/jpg/02/15/84/43/500_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                "Jane Doe",
                Timestamp.now(),
                "https://as2.ftcdn.net/v2/jpg/02/15/84/43/500_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg"
            ),
            Post(
                "https://as2.ftcdn.net/v2/jpg/02/15/84/43/500_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                "Jane Doe",
                Timestamp.now(),
                "https://as2.ftcdn.net/v2/jpg/02/15/84/43/500_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg"
            ),
            Post(
                "https://as2.ftcdn.net/v2/jpg/02/15/84/43/500_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                "Jane Doe",
                Timestamp.now(),
                "https://as2.ftcdn.net/v2/jpg/02/15/84/43/500_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg"
            )
        )

        binding.rvHome.adapter = HomeScreenAdapter(postList)
    }

}