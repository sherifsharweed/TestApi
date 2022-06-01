package com.shekoo.testapi.ui.get

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shekoo.testapi.databinding.FragmentGetBinding
import com.shekoo.testapi.ui.post.BodyAdapter
import com.shekoo.testapi.utility.Header
import com.shekoo.testapi.utility.Network
import com.shekoo.testapi.utility.PostBody

class GetFragment : Fragment() {

    private lateinit var binding: FragmentGetBinding
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var headerListItems : MutableList<Header>
    private val getViewModel: GetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGetBinding.inflate(inflater, container, false)
        createAdapter()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        headerListItems = mutableListOf()

        binding.apply {
            addHeaderButton.setOnClickListener {
                if(headerKeyEditText.text.isNotEmpty()&&headerValueEditText.text.isNotEmpty()){
                    headerListItems.add(Header(headerKeyEditText.text.toString(),headerValueEditText.text.toString()))
                    headerAdapter.addList(headerListItems)
                    headerKeyEditText.text.clear()
                    headerValueEditText.text.clear()
                }
            }

            getButton.setOnClickListener {
                if (Network.hasInternet(requireContext())) {
                    if (getUrlText.text.isNotEmpty()) {
                        Thread {
                            getViewModel.getMethod(getUrlText.text.toString(),headerListItems)
                        }.start()
                    }else{
                        Toast.makeText(requireContext(), "Enter URL", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Check Your Connection", Toast.LENGTH_SHORT).show()
                }
            }
        }


        getViewModel.getResponse.observe(viewLifecycleOwner, Observer {
            binding.getResponseTextView.text = it
        })
    }

    private fun createAdapter(){
        headerAdapter = HeaderAdapter()
        binding.headerRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.headerRecyclerView.adapter = headerAdapter
    }


}