package com.shekoo.testapi.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shekoo.testapi.databinding.FragmentPostBinding
import com.shekoo.testapi.utility.Network
import com.shekoo.testapi.utility.PostBody

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var bodyAdapter: BodyAdapter
    private lateinit var bodyListItems : MutableList<PostBody>
    private val postViewModel : PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
    }

    override fun onResume() {
        super.onResume()
        bodyListItems = mutableListOf()
        binding.apply {
            addBodyButton.setOnClickListener {
                if(keyEditText.text.isNotEmpty()&&valueEditText.text.isNotEmpty()){
                    bodyListItems.add(PostBody(keyEditText.text.toString(),valueEditText.text.toString()))
                    bodyAdapter.addList(bodyListItems)
                    keyEditText.text.clear()
                    valueEditText.text.clear()
                }
            }
            postButton.setOnClickListener {
                if (Network.hasInternet(requireContext())) {
                    if (binding.postUrlText.text.isNotEmpty()) {
                        Thread {
                            postViewModel.postMethod(binding.postUrlText.text.toString(),bodyListItems)
                        }.start()
                    }else{
                        Toast.makeText(requireContext(), "Enter URL", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Check Your Connection", Toast.LENGTH_SHORT).show()
                }
            }
        }

        postViewModel.postResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.postResponseTextView.text = it
        })


    }

    private fun createAdapter(){
        bodyAdapter = BodyAdapter()
        binding.bodyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.bodyRecyclerView.adapter = bodyAdapter
    }


}