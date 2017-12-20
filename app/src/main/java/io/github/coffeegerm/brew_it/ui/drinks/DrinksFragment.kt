/*
 * Copyright 2017 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.brew_it.ui.drinks

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.coffeegerm.brew_it.BetterBaristaApp.Companion.syringe
import io.github.coffeegerm.brew_it.R
import io.github.coffeegerm.brew_it.data.Drink
import kotlinx.android.synthetic.main.fragment_drinks.*


/**
 * Fragment Responsible for controlling the Recycler view of drinks
 * As well as beginning the SingleDrinkActivity
 */

class DrinksFragment : Fragment() {
  
  lateinit var drinksViewModel: DrinksViewModel
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_drinks, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    syringe.inject(this)
    drinksViewModel = ViewModelProviders.of(this).get(DrinksViewModel::class.java)
    subscribe()
  }
  
  private fun subscribe() {
    val drinksObserver = Observer<ArrayList<Drink>> { drinks -> setupAdapter(drinks!!) }
    drinksViewModel.drinks.observe(this, drinksObserver)
  }
  
  private fun setupAdapter(drinks: ArrayList<Drink>) {
    drinks_recycler_view.layoutManager = GridLayoutManager(activity, 2)
    val adapter = DrinksAdapter()
    drinks_recycler_view.adapter = adapter
    adapter.setDrinks(drinks)
  }
}