package com.shekoo.testapi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.shekoo.testapi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            button.setOnClickListener {
                if(urlText.text.isNotEmpty()){
                    Thread {
                        homeViewModel.getMethod(urlText.text.toString())
                    }.start()

                }
            }
        }
        homeViewModel.getResponse.observe(viewLifecycleOwner, Observer {
            binding.getResponseTextView.text = it
        })
    }


}