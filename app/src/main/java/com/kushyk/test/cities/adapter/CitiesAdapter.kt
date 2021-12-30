package com.kushyk.test.cities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.kushyk.test.cities.viewmodel.CityModel
import com.kushyk.test.databinding.CityItemBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CitiesAdapter : ListAdapter<CityModel, CityViewHolder>(
    CityItemCallback()
) {

    private val _actionFlow = MutableSharedFlow<ViewHolderAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder =
        CityViewHolder(
            CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            _actionFlow
        )

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class CityItemCallback : ItemCallback<CityModel>() {
    override fun areItemsTheSame(oldItem: CityModel, newItem: CityModel): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: CityModel, newItem: CityModel): Boolean =
        oldItem == newItem
}