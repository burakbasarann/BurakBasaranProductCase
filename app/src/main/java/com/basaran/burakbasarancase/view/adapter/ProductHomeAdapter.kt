package com.basaran.burakbasarancase.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.basaran.burakbasarancase.common.util.ClickAction
import com.basaran.burakbasarancase.data.model.Products
import com.basaran.burakbasarancase.databinding.ItemProductBinding
import com.basaran.burakbasarancase.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso

class ProductHomeAdapter(
    private var products: List<Products>,
    private val context: Context,
    private val viewModel: HomeViewModel,
    private val onItemClick: (ClickAction, Products) -> Unit
) : RecyclerView.Adapter<ProductHomeAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemProductBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductHomeAdapter.MyViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHomeAdapter.MyViewHolder, position: Int) {
        val product = products[position]

        with(holder.binding) {
            txtPrice.text = product.price.toString() + " ₺"
            txtBrand.text = product.name

            Picasso.get().load(product.image).into(imgProduct)

            val favList = viewModel.productToFavoriteProduct(product)
            updateFavVisibility(viewModel.isFavorite(favList), imgFavEmpty, imgFavFull)

            val basketList = viewModel.productToBasketProduct(product)
            updateBasketVisibility(viewModel.isBasket(basketList), btnAddToCard, btnRemoveToCard)

            holder.itemView.setOnClickListener {
                product.id?.let { it1 -> onItemClick.invoke(ClickAction.GO_TO_DETAILS, product) }
            }

            setOnClickListener(imgFavEmpty, imgFavEmpty, imgFavFull, ClickAction.FAVORITE_ADD, product, "Added Favorite ${product.name}")
            setOnClickListener(imgFavFull, imgFavFull, imgFavEmpty, ClickAction.FAVORITE_REMOVE, product, "Deleted Favorite ${product.name}")
            setOnClickListener(btnAddToCard, btnAddToCard, btnRemoveToCard, ClickAction.ADD_TO_CART, product ,"Added Basket ${product.name}")
            setOnClickListener(btnRemoveToCard, btnRemoveToCard, btnAddToCard, ClickAction.REMOVE_FROM_CART, product, "Deleted Basket ${product.name}")
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    private fun setOnClickListener(
        clickView: View,
        goneView: View,
        visibleView: View,
        clickAction: ClickAction,
        products: Products,
        text: String,
    ) {
        clickView.setOnClickListener {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            onItemClick.invoke(clickAction, products)
            goneView.visibility = View.GONE
            visibleView.visibility = View.VISIBLE
        }
    }

    private fun updateFavVisibility(isFavorite: Boolean, imgFavEmpty: ImageView, imgFavFull: ImageView) {
        imgFavEmpty.visibility = if (isFavorite) View.GONE else View.VISIBLE
        imgFavFull.visibility = if (isFavorite) View.VISIBLE else View.GONE
    }

    private fun updateBasketVisibility(isInBasket: Boolean, btnAddToCard: Button, btnRemoveToCard: Button) {
        btnAddToCard.visibility = if (isInBasket) View.GONE else View.VISIBLE
        btnRemoveToCard.visibility = if (isInBasket) View.VISIBLE else View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Products>) {
        products = newList
        notifyDataSetChanged()
    }
}