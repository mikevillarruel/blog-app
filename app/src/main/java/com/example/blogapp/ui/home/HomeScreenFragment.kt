package com.example.blogapp.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.blogapp.R
import com.example.blogapp.core.Result
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.data.remote.home.HomeScreenDataSource
import com.example.blogapp.databinding.FragmentHomeScreenBinding
import com.example.blogapp.domain.home.HomeScreenRepoImpl
import com.example.blogapp.presentation.home.HomeScreenViewModel
import com.example.blogapp.presentation.home.HomeScreenViewModelFactory
import com.example.blogapp.ui.home.adapter.HomeScreenAdapter

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepoImpl(
                HomeScreenDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewModel.fetchLatestPosts().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.show()
                        return@Observer
                    } else {
                        binding.emptyContainer.hide()
                    }
                    binding.rvHome.adapter = HomeScreenAdapter(result.data)
                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


    }

}