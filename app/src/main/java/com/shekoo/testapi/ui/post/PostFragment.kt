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
import com.shekoo.testapi.ui.get.HeaderAdapter
import com.shekoo.testapi.utility.Header
import com.shekoo.testapi.utility.Network
import com.shekoo.testapi.utility.PostBody

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var bodyAdapter: BodyAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var headerListItems : MutableList<Header>
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
        headerListItems = mutableListOf()

        binding.apply {

            addHeaderButton2.setOnClickListener {
                if(headerKeyEditText.text.isNotEmpty()&&headerValueEditText.text.isNotEmpty()){
                    headerListItems.add(Header(headerKeyEditText.text.toString(),headerValueEditText.text.toString()))
                    headerAdapter.addList(headerListItems)
                    headerKeyEditText.text.clear()
                    headerValueEditText.text.clear()
                }
            }

            addBodyButton.setOnClickListener {
                if(bodyKeyEditText.text.isNotEmpty()&&bodyValueEditText.text.isNotEmpty()){
                    bodyListItems.add(PostBody(bodyKeyEditText.text.toString(),bodyValueEditText.text.toString()))
                    bodyAdapter.addList(bodyListItems)
                    bodyKeyEditText.text.clear()
                    bodyValueEditText.text.clear()
                }
            }
            postButton.setOnClickListener {
                if (Network.hasInternet(requireContext())) {
                    if (binding.postUrlText.text.isNotEmpty()) {
                        Thread {
                            postViewModel.postMethod(binding.postUrlText.text.toString(),bodyListItems,headerListItems)
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
        headerAdapter = HeaderAdapter()
        binding.bodyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.bodyRecyclerView.adapter = bodyAdapter

        binding.headerRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.headerRecyclerView.adapter=headerAdapter
    }


}