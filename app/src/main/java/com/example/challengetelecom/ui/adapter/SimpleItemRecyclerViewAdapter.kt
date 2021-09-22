package com.example.challengetelecom.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.databinding.ItemListContentBinding

class SimpleItemRecyclerViewAdapter(
    private val characters: ArrayList<Character>,
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = characters[position]
        holder.imageView.apply {
            Glide.with(context).load(item.image).thumbnail(0.5f)
                .placeholder(android.R.color.darker_gray).into(this)
        }
        holder.idView.text = item.name
        holder.contentView.text = item.origin.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
        }
    }

    override fun getItemCount() = characters.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivImage
        val idView: TextView = binding.tvName
        val contentView: TextView = binding.content
    }

    fun addPage(newItems: List<Character>) {
        val size = characters.size
        characters.addAll(newItems)
        notifyItemRangeInserted(size, characters.size)
    }
}