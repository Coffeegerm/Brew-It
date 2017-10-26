package io.github.coffeegerm.brew_it

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.coffeegerm.brew_it.data.Drink

/**
 * Created by dYarzebinski on 10/26/2017.
 * TODO: Add class comment header
 */
class DrinksAdapter() : RecyclerView.Adapter<DrinksViewHolder>() {

    private val drinks: MutableList<Drink> = mutableListOf()

    override fun onBindViewHolder(holder: DrinksViewHolder, position: Int) {
        holder.bindDrink(drinks[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drink, parent, false)
        return DrinksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return drinks.size
    }
}
