package com.shekoo.testapi.ui.get

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.shekoo.testapi.databinding.FragmentGetBinding
import com.shekoo.testapi.utility.Network

class GetFragment : Fragment() {

    private lateinit var binding: FragmentGetBinding
    private val getViewModel: GetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
            binding.getButton.setOnClickListener {
                if (Network.hasInternet(requireContext())) {
                    if (binding.getUrlText.text.isNotEmpty()) {
                        Thread {
                            getViewModel.getMethod(binding.getUrlText.text.toString())
                        }.start()

                    }
                }else{
                    Toast.makeText(requireContext(), "Check Your Connection", Toast.LENGTH_SHORT).show()
                }
            }

        getViewModel.getResponse.observe(viewLifecycleOwner, Observer {
            binding.getResponseTextView.text = it
        })
    }


}