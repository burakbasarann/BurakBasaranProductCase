package com.basaran.burakbasarancase.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.basaran.burakbasarancase.data.model.ProductsBasket
import com.basaran.burakbasarancase.databinding.ItemProductBasketBinding
import com.basaran.burakbasarancase.viewmodel.ProductBasketViewModel
import com.squareup.picasso.Picasso

class ProductBasketAdapter(
    private var orderProductsList: MutableList<ProductsBasket>,
    var context: Context,
    private var viewModel: ProductBasketViewModel
) : RecyclerView.Adapter<ProductBasketAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemProductBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemProductBasketBinding

        init {
            this.binding = binding
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        orderProductsList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = ItemProductBasketBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val products = orderProductsList[position]
        val t = holder.binding
        t.titleTextView.text = products.brand
        t.priceTextview.text = products.price.toString()
        t.txtPiece.text = products.productsPiece.toString()
        viewModel.updateTotalValues()
        Picasso.get().load(products.image).into(t.productImageView)
        t.btnDecrease.setOnClickListener {
            if (products.productsPiece == 1) {
                viewModel.deleteBasketItem(products)
                Toast.makeText(context, products.name + "Silindi", Toast.LENGTH_SHORT)
                    .show()
                removeItem(position)
                viewModel.updateTotalValues()
                viewModel.loadBasketProducts()
            } else {
                products.productsPiece--
                t.txtPiece.text = products.productsPiece.toString()
                viewModel.updateTotalValues()
                viewModel.updateBasketItem(products)
            }
        }
        t.btnDelete.setOnClickListener {
            viewModel.deleteBasketItem(products)
            products.productsPiece = 0
            Toast.makeText(context, products.name + " Silindi", Toast.LENGTH_SHORT)
                .show()
            removeItem(position)
            viewModel.updateTotalValues()
            viewModel.loadBasketProducts()
        }
        t.btnIncrease.setOnClickListener {
            products.productsPiece++
            viewModel.updateTotalValues()
            t.txtPiece.text = products.productsPiece.toString()
            viewModel.updateBasketItem(products)
        }
    }

    override fun getItemCount(): Int {
        return orderProductsList.size
    }
}