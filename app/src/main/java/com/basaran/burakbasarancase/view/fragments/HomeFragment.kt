package com.basaran.burakbasarancase.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.basaran.burakbasarancase.common.FilterDialog
import com.basaran.burakbasarancase.common.UIState
import com.basaran.burakbasarancase.common.util.ClickAction
import com.basaran.burakbasarancase.common.util.hideProgress
import com.basaran.burakbasarancase.common.util.showProgress
import com.basaran.burakbasarancase.databinding.FragmentHomeBinding
import com.basaran.burakbasarancase.view.activities.MainActivity
import com.basaran.burakbasarancase.view.adapter.ProductHomeAdapter
import com.basaran.burakbasarancase.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: ProductHomeAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mainActivity = activity as MainActivity

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    binding.progressBar.showProgress()
                    binding.rvProducts.visibility = View.GONE
                }

                is UIState.Success -> {
                    binding.progressBar.hideProgress()
                    binding.rvProducts.visibility = View.VISIBLE
                }

                is UIState.Error -> {
                    binding.progressBar.hideProgress()
                    Toast.makeText(requireContext(), "Service Error", Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
        binding.searchBar.addTextChangedListener {
            viewModel.searchProducts(it.toString())
        }

        viewModel.favProductsList.observe(viewLifecycleOwner) {
            viewModel.loadFavProducts()
            mainActivity.updateFavBadgeCount(viewModel.favProductsList.value?.size ?: 0)
        }

        viewModel.basketList.observe(viewLifecycleOwner) {
            viewModel.loadBasketProducts()
            mainActivity.updateBasketBadgeCount(viewModel.basketList.value?.size ?: 0)
        }

        viewModel.filteredList.observe(viewLifecycleOwner) { products ->
            if (products != null) {
                homeAdapter.updateList(products)
            } else {
                Toast.makeText(requireContext(), "Filter List is Empty", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.products.observe(viewLifecycleOwner) {
            homeAdapter = ProductHomeAdapter(it, requireContext(), viewModel) { action, product ->
                when (action) {
                    ClickAction.GO_TO_DETAILS -> {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
                                product.id!!.toInt()
                            )
                        )
                    }

                    ClickAction.ADD_TO_CART -> {
                        val basketList = viewModel.productToBasketProduct(product)
                        viewModel.insertBasketItem(basketList)
                    }

                    ClickAction.REMOVE_FROM_CART -> {
                        val basketList = viewModel.productToBasketProduct(product)
                        viewModel.deleteBasketItem(basketList)
                    }

                    ClickAction.FAVORITE_ADD -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val favoriteProduct = viewModel.productToFavoriteProduct(product)
                            viewModel.insertFavProducts(favoriteProduct)
                        }
                    }

                    ClickAction.FAVORITE_REMOVE -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val favoriteProduct = viewModel.productToFavoriteProduct(product)
                            viewModel.deleteFavProducts(favoriteProduct)
                        }
                    }
                }
            }
            binding.rvProducts.adapter = homeAdapter
        }

        binding.btnFilter.setOnClickListener {
            val brandList = ArrayList<String>()
            brandList.add("Please Select Brand")
            viewModel.products.value!!.forEach {
                it.brand?.let { brand ->
                    brandList.add(brand)
                }
            }
            val dialog = FilterDialog(requireContext(), brandList) { filterOptions, clearButton ->
                if (clearButton) {
                    viewModel.getProducts()
                } else {
                    viewModel.applyFilter(filterOptions)
                }
            }
            dialog.show(childFragmentManager, "FilterDialog")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavProducts()
        viewModel.loadBasketProducts()
        viewModel.refreshProducts()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}