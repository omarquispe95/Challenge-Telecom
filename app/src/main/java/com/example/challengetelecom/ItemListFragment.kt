package com.example.challengetelecom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.data.network.Resource
import com.example.challengetelecom.databinding.FragmentItemListBinding
import com.example.challengetelecom.ui.adapter.SimpleItemRecyclerViewAdapter
import com.example.challengetelecom.ui.main.ItemListViewModel
import com.example.challengetelecom.util.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

@AndroidEntryPoint
class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<ItemListViewModel>()

    private var charactersAdapter: SimpleItemRecyclerViewAdapter? = null
    private var isLastPage = false
    private var isScrolling = false
    private var isLoading = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.LIMIT_PAGE
            val shouldPaginated =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginated) {
                isScrolling = false
                viewModel.getCharacters()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Character
            val bundle = Bundle()
            bundle.putSerializable(
                ItemDetailFragment.ARG_ITEM,
                item
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as Character
            Toast.makeText(
                v.context,
                "Context click of item " + item.id,
                Toast.LENGTH_LONG
            ).show()
            true
        }
        setupRecyclerView(recyclerView, onClickListener, onContextClickListener)
        //viewModel.getCharacters()
        viewModel.getCharactersLiveData().observe(viewLifecycleOwner) { observeResults(it) }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener
    ) {
        charactersAdapter = SimpleItemRecyclerViewAdapter(
            arrayListOf(),
            onClickListener,
            onContextClickListener
        )
        recyclerView.apply {
            adapter = charactersAdapter
            addOnScrollListener(this@ItemListFragment.scrollListener)
        }
    }

    private fun observeResults(resource: Resource<List<Character>>) {
        when (resource) {
            is Resource.Success -> {
                showProgress(false)
                resource.data?.let {
                    isLastPage = it.size < Constants.LIMIT_PAGE
                    charactersAdapter?.addPage(it)
                }
            }
            is Resource.Error -> {
                showProgress(false)
                resource.message?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() }
            }
            is Resource.Loading -> { showProgress(true) }
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar?.visibility = if (show) View.VISIBLE else View.INVISIBLE
        isLoading = show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}