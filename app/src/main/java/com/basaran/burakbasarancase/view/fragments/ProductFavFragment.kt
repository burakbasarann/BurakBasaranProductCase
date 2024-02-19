package com.basaran.burakbasarancase.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.basaran.burakbasarancase.common.UIState
import com.basaran.burakbasarancase.common.util.ClickAction
import com.basaran.burakbasarancase.common.util.hideProgress
import com.basaran.burakbasarancase.common.util.showProgress
import com.basaran.burakbasarancase.databinding.FragmentProductFavBinding
import com.basaran.burakbasarancase.view.activities.MainActivity
import com.basaran.burakbasarancase.view.adapter.ProductFavAdapter
import com.basaran.burakbasarancase.viewmodel.ProductFavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFavFragment : Fragment() {

    private var _binding: FragmentProductFavBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductFavViewModel by viewModels()
    private lateinit var favProductsAdapter: ProductFavAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductFavBinding.inflate(inflater, container, false)
        val view = binding.root
        val mainActivity = activity as MainActivity

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    binding.progressBar.showProgress()
                }

                is UIState.Success -> {
                    binding.progressBar.hideProgress()
                }

                is UIState.Error -> {
                    binding.progressBar.hideProgress()
                    Toast.makeText(requireContext(), "Service Error", Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }

        viewModel.favProductsList.observe(viewLifecycleOwner) { productFavList ->
            favProductsAdapter =
                ProductFavAdapter(productFavList, requireContext(), viewModel) { action, product, position ->
                    when (action) {
                        ClickAction.GO_TO_DETAILS -> {
                            findNavController().navigate(
                                ProductFavFragmentDirections.actionProductFavFragmentToProductDetailFragment(
                                    product.id!!.toInt()
                                )
                            )
                        }

                        ClickAction.ADD_TO_CART -> {
                            val favoriteProduct = viewModel.productFavToBasketProduct(product)
                            viewModel.insertBasketItem(favoriteProduct)
                        }

                        ClickAction.REMOVE_FROM_CART -> {
                            val favoriteProduct = viewModel.productFavToBasketProduct(product)
                            viewModel.deleteBasketItem(favoriteProduct)
                        }

                        ClickAction.FAVORITE_REMOVE -> {
                            viewModel.deleteFavProducts(product)
                            favProductsAdapter.removeItem(position)
                            mainActivity.updateFavBadgeCount(viewModel.favProductsList.value?.size ?: 0)
                            viewModel.loadFavProducts()
                        }

                        else -> {}
                    }
                }
            binding.rvProducts.adapter = favProductsAdapter
            if (productFavList.isNullOrEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.rvProducts.visibility = View.GONE
            }else {
                binding.noDataText.visibility = View.GONE
                binding.rvProducts.visibility = View.VISIBLE
            }
        }

        viewModel.basketList.observe(viewLifecycleOwner) {
            viewModel.loadBasketProducts()
            mainActivity.updateBasketBadgeCount(viewModel.basketList.value?.size ?: 0)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavProducts()
    }
}