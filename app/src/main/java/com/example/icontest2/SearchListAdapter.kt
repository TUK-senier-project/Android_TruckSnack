package com.example.icontest2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SearchListAdapter(private val items: ArrayList<SearchListDTO>): RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>()
{
    class SearchViewHolder(v: View): RecyclerView.ViewHolder(v)
    {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item:SearchListDTO)
        {
            view.findViewById<TextView>(R.id.search_title_tv).text = item.title
            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListAdapter.SearchViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchListAdapter.SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListAdapter.SearchViewHolder, position: Int)
    {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "${item.title}을 클릭함", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size
}