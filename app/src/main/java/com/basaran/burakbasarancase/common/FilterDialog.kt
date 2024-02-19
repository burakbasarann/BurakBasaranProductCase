package com.basaran.burakbasarancase.common

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.basaran.burakbasarancase.data.model.FilterOptions
import com.basaran.burakbasarancase.databinding.ItemFilterDialogBinding

class FilterDialog(private val context: Context, private val brands: List<String>, private val onApply: (FilterOptions, Boolean) -> Unit) : DialogFragment() {

    private var _binding: ItemFilterDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ItemFilterDialogBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, brands)
        binding.brands.adapter = adapter
        var selectedBrand = ""
        binding.brands.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedBrand = brands[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.applyButton.setOnClickListener {
            val minPrice = binding.minPrice.text.toString().toDoubleOrNull() ?: 0.0
            val maxPrice = binding.maxPrice.text.toString().toDoubleOrNull() ?: Double.MAX_VALUE
            onApply.invoke(FilterOptions(minPrice, maxPrice, selectedBrand), false)
            dismiss()
        }

        binding.filterClearButton.setOnClickListener {
            onApply.invoke(FilterOptions(0.0, Double.MAX_VALUE, ""), true)
            dismiss()
        }

        return binding.root
    }
}