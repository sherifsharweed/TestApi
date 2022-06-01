package com.shekoo.testapi.ui.get

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shekoo.testapi.R
import com.shekoo.testapi.utility.Header
import com.shekoo.testapi.utility.PostBody


class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {


    private val items: MutableList<Header> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_key_value, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keyTextView.text = items[position].key
        holder.valueTextView.text = items[position].value


    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addList(listOfItems: List<Header>) {
        items.clear()
        items.addAll(listOfItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val keyTextView: TextView
            get() = itemView.findViewById(R.id.keyTextView)

        val valueTextView: TextView
            get() = itemView.findViewById(R.id.valueTextView)


    }
}