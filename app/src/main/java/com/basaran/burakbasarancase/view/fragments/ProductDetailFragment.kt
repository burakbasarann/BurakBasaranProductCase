package com.basaran.burakbasarancase.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.basaran.burakbasarancase.common.UIState
import com.basaran.burakbasarancase.common.util.hideProgress
import com.basaran.burakbasarancase.common.util.showProgress
import com.basaran.burakbasarancase.data.model.Products
import com.basaran.burakbasarancase.databinding.FragmentProductDetailBinding
import com.basaran.burakbasarancase.view.activities.MainActivity
import com.basaran.burakbasarancase.viewmodel.BaseViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BaseViewModel by viewModels()
    private lateinit var productId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId.toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val mainActivity = activity as MainActivity

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

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
        viewModel.products.observe(viewLifecycleOwner) {productList ->
            for(product in productList){
                if (product.id == productId){
                    Picasso.get().load(product.image).into(binding.imgDetail)
                    with(binding) {
                        toolbarTitle.text = product.name
                        tvTitle.text = product.name
                        tvBrandName.text = product.brand
                        tvDescription.text = product.description
                        tvPrice.text = product.price

                        viewModel.favProductsList.value?.forEach{
                            if (product.id!!.toInt() == it.id){
                                imgBtnFavorite.visibility = View.GONE
                                imgBtnFavoriteFull.visibility = View.VISIBLE
                            }
                        }

                        viewModel.basketList.value?.forEach{
                            if (product.id!!.toInt() == it.id){
                                btnAddToCard.visibility = View.INVISIBLE
                                btnRemoveToCard.visibility = View.VISIBLE
                            }
                        }

                        viewModel.favProductsList.observe(viewLifecycleOwner) {
                            viewModel.loadFavProducts()
                            mainActivity.updateFavBadgeCount(viewModel.favProductsList.value?.size ?: 0)
                        }

                        viewModel.basketList.observe(viewLifecycleOwner) {
                            viewModel.loadBasketProducts()
                            mainActivity.updateBasketBadgeCount(viewModel.basketList.value?.size ?: 0)
                        }

                        imgBtnFavorite.setOnClickListener {
                            clickedFav(true, imgBtnFavorite, imgBtnFavoriteFull, product)
                        }
                        imgBtnFavoriteFull.setOnClickListener {
                            clickedFav(false, imgBtnFavoriteFull, imgBtnFavorite, product)
                        }
                        btnAddToCard.setOnClickListener {
                            clickedBasket(true, btnAddToCard, btnRemoveToCard, product)
                        }
                        btnRemoveToCard.setOnClickListener {
                            clickedBasket(false, btnRemoveToCard, btnAddToCard, product)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun clickedFav(isInsert: Boolean, goneView: ImageView, visibleView: ImageView, products: Products){
        goneView.visibility = View.GONE
        visibleView.visibility = View.VISIBLE
        val list = viewModel.productToFavoriteProduct(products)
        if (isInsert){
            viewModel.insertFavProducts(list)
            Toast.makeText(context, "Added Favorite ${products.name}", Toast.LENGTH_SHORT).show()
        }else {
            viewModel.deleteFavProducts(list)
            Toast.makeText(context, "Deleted Favorite ${products.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clickedBasket(isInsert: Boolean, goneView: Button, visibleView: Button, products: Products){
        goneView.visibility = View.INVISIBLE
        visibleView.visibility = View.VISIBLE
        val list = viewModel.productToBasketProduct(products)
        if (isInsert){
            viewModel.insertBasketItem(list)
            Toast.makeText(context, "Added Basket ${products.name}", Toast.LENGTH_SHORT).show()
        }else {
            viewModel.deleteBasketItem(list)
            Toast.makeText(context, "Deleted Basket ${products.name}", Toast.LENGTH_SHORT).show()
        }
    }
}