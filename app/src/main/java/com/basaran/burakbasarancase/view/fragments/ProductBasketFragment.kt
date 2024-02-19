package com.basaran.burakbasarancase.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.basaran.burakbasarancase.databinding.FragmentProductBasketBinding
import com.basaran.burakbasarancase.view.activities.MainActivity
import com.basaran.burakbasarancase.view.adapter.ProductBasketAdapter
import com.basaran.burakbasarancase.viewmodel.ProductBasketViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductBasketFragment : Fragment() {

    private var _binding: FragmentProductBasketBinding? = null
    private val binding get() = _binding!!
    private lateinit var productBasketAdapter: ProductBasketAdapter
    private val viewModel: ProductBasketViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBasketBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity

        viewModel.basketList.observe(viewLifecycleOwner) { basketList ->
            productBasketAdapter = ProductBasketAdapter(basketList, requireContext(), viewModel)
            binding.rvBasket.adapter = productBasketAdapter
            mainActivity.updateBasketBadgeCount(basketList.size)

            if (basketList.isNullOrEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.rvBasket.visibility = View.GONE
            }else {
                binding.noDataText.visibility = View.GONE
                binding.rvBasket.visibility = View.VISIBLE
            }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.txtCount.text = it.toString()
        }

        binding.btnCompleted.setOnClickListener {
            if (viewModel.basketList.value.isNullOrEmpty()) {
                showDialog("Cart Empty", "Please add something to the cart.") {
                    findNavController().popBackStack()
                }
            } else {
                viewModel.allDeleteBasket()
                showDialog("Completed", "Your shopping is completed.") {
                    findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadBasketProducts()
    }

    private fun showDialog(title: String, message: String, onClick :() -> Unit) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Okay") { dialog, _ ->
                onClick.invoke()
                dialog.dismiss()
            }
        dialog.show()
    }
}