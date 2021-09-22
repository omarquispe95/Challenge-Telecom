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
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

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
        holder.tvName.text = item.name
        holder.tvStatusSpecies.text = String.format("%s - %s", item.status, item.species)
        holder.tvLastLocation.text = item.location.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = characters.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivImage
        val tvName: TextView = binding.tvName
        val tvStatusSpecies: TextView = binding.tvStatusSpecies
        val tvLastLocation: TextView = binding.tvLastLocation
    }

    fun addPage(newItems: List<Character>) {
        val size = characters.size
        characters.addAll(newItems)
        notifyItemRangeInserted(size, characters.size)
    }
}