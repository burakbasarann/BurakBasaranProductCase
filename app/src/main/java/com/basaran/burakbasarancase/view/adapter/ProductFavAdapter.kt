package com.basaran.burakbasarancase.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.basaran.burakbasarancase.common.util.ClickAction
import com.basaran.burakbasarancase.data.model.ProductsFav
import com.basaran.burakbasarancase.databinding.ItemProductFavBinding
import com.basaran.burakbasarancase.viewmodel.ProductFavViewModel
import com.squareup.picasso.Picasso

class ProductFavAdapter(
    private var productsList: List<ProductsFav>,
    var context: Context,
    private val viewModel: ProductFavViewModel,
    private val onItemClick: (ClickAction, ProductsFav, Int) -> Unit
) :
    RecyclerView.Adapter<ProductFavAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemProductFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemProductFavBinding

        init {
            this.binding = binding
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        val tempList: MutableList<ProductsFav> = productsList as MutableList<ProductsFav>
        tempList.removeAt(position)
        productsList = tempList
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val tasarim = ItemProductFavBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(tasarim)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productsList[position]

        with(holder.binding) {
            txtPrice.text = product.price.toString() + " ₺"
            txtBrand.text = product.name

            Picasso.get().load(product.image).into(imgProduct)



            val basketList = viewModel.productFavToBasketProduct(product)
            updateBasketVisibility(viewModel.isBasket(basketList), btnAddToCard, btnRemoveToCard)

            setOnClickListener(btnAddToCard, btnAddToCard, btnRemoveToCard, ClickAction.ADD_TO_CART, product, "Added To Card ${product.name}", position)
            setOnClickListener(btnRemoveToCard, btnRemoveToCard, btnAddToCard, ClickAction.REMOVE_FROM_CART, product, "Removed To Card ${product.name}", position)
            setOnClickListener(imgFavFull, imgFavFull, imgFavFull, ClickAction.FAVORITE_REMOVE, product, "Removed To Favorite ${product.name}", position)

            holder.itemView.setOnClickListener {
                product.id?.let { it1 ->
                    onItemClick.invoke(ClickAction.GO_TO_DETAILS, product, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    private fun updateBasketVisibility(isInBasket: Boolean, btnAddToCard: Button, btnRemoveToCard: Button) {
        btnAddToCard.visibility = if (isInBasket) View.GONE else View.VISIBLE
        btnRemoveToCard.visibility = if (isInBasket) View.VISIBLE else View.GONE
    }

    private fun setOnClickListener(
        clickView: View,
        goneView: View,
        visibleView: View,
        clickAction: ClickAction,
        products: ProductsFav,
        text: String,
        position: Int
    ) {
        clickView.setOnClickListener {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            onItemClick.invoke(clickAction, products,position)
            goneView.visibility = View.GONE
            visibleView.visibility = View.VISIBLE
        }
    }

}