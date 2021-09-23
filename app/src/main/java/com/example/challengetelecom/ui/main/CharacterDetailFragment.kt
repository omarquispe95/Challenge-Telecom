package com.example.challengetelecom.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private var item: Character? = null
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                item = it.getParcelable(ARG_ITEM) as Character?
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.apply {
            //toolbarLayout?.title = item?.name

            item?.let { character ->
                Glide.with(requireContext()).load(character.image).thumbnail(0.5f)
                    .placeholder(android.R.color.darker_gray).into(ivImage)
                tvName.text = character.name
                tvStatusSpecies.text = String.format("%s - %s", character.status, character.species)
                tvLastLocation.text = character.location?.name
                tvFirstLocation.text = character.origin?.name
            }
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM = "item"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}