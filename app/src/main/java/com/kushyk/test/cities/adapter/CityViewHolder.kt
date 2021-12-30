package com.kushyk.test.cities.adapter

import androidx.recyclerview.widget.RecyclerView
import com.kushyk.test.cities.viewmodel.CityModel
import com.kushyk.test.databinding.CityItemBinding
import kotlinx.coroutines.flow.MutableSharedFlow

class CityViewHolder(
    private val binding: CityItemBinding,
    private val actionFlow: MutableSharedFlow<ViewHolderAction>
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var model: CityModel

    init {
        binding.root.setOnClickListener {
            actionFlow.tryEmit(ViewHolderAction.Click(model.id))
        }
    }

    fun bind(model: CityModel) {
        this.model = model
        binding.apply {
            titleView.text = model.title
            descriptionView.text = model.description
        }
    }
}