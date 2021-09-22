package com.example.challengetelecom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.databinding.FragmentItemDetailBinding

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: Character? = null

    lateinit var itemDetailTextView: TextView

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = it.getSerializable(ARG_ITEM) as Character
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.toolbarLayout?.title = item?.name

        itemDetailTextView = binding.itemDetail
        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = it.origin.name
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM = "item"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}