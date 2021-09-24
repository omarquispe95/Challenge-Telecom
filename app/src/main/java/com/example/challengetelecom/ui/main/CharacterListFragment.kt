package com.example.challengetelecom.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengetelecom.R
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.data.network.Resource
import com.example.challengetelecom.data.repository.MainRepository
import com.example.challengetelecom.databinding.FragmentCharacterListBinding
import com.example.challengetelecom.ui.adapter.CharacterAdapter
import com.example.challengetelecom.util.Constants
import com.example.challengetelecom.util.isFirstItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var repository: MainRepository
    private val viewModel by viewModels<ItemListViewModel>()

    private var charactersAdapter: CharacterAdapter? = null
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
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.itemDetailNavContainer)
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Character
            val bundle = Bundle()
            bundle.putParcelable(
                CharacterDetailFragment.ARG_ITEM,
                item
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        setupRecyclerView(binding.rvCharacter, onClickListener)
        viewModel.getCharactersLiveData().observe(viewLifecycleOwner) { observeResults(it) }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener
    ) {
        if (charactersAdapter == null)
            charactersAdapter = CharacterAdapter(
                arrayListOf(),
                onClickListener
            )
        recyclerView.apply {
            val margin8dp = resources.getDimensionPixelOffset(R.dimen.container_margin)
            adapter = charactersAdapter
            addOnScrollListener(this@CharacterListFragment.scrollListener)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State,
                ) {
                    with(outRect) {
                        left = margin8dp
                        right = margin8dp
                        top = if (parent.isFirstItem(view)) margin8dp else 0
                        bottom = margin8dp
                    }
                }
            })
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
        binding.progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
        isLoading = show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        charactersAdapter?.getList()?.let {
            outState.putParcelableArrayList(Constants.ITEMS, it)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getParcelableArrayList<Character>(Constants.ITEMS)?.let {
            charactersAdapter?.submitList(it.toList())
        }
    }
}