package com.example.challengetelecom.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.databinding.ItemListContentBinding

class CharacterAdapter(
    private val characters: ArrayList<Character>,
    private val onClick:(View, Character) -> Unit
) :
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = characters[position]
        with(holder) {
            Glide.with(imageView.context).load(item.image).thumbnail(0.5f)
                .placeholder(android.R.color.darker_gray).into(imageView)
            tvName.text = item.name
            itemView.setOnClickListener { onClick(itemView, item) }
        }
    }

    override fun getItemCount() = characters.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivImage
        val tvName: TextView = binding.tvName
    }

    fun getList() = characters

    fun submitList(newItems: List<Character>) {
        characters.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addPage(newItems: List<Character>) {
        if (newItems.isNotEmpty()) {
            if (!characters.contains(newItems.first())) {
                val size = characters.size
                characters.addAll(newItems)
                notifyItemRangeInserted(size, characters.size)
            }
        }
    }
}