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

package io.github.coffeegerm.betterbarista.ui.children.drinks.singleDrink

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import io.github.coffeegerm.betterbarista.R
import io.github.coffeegerm.betterbarista.data.model.Drink
import io.github.coffeegerm.betterbarista.utilities.Constants
import kotlinx.android.synthetic.main.activity_single_drink.*

class SingleDrinkActivity : AppCompatActivity() {
  
  private var singleDrinkAdapter: SingleDrinkAdapter = SingleDrinkAdapter()
  
  private lateinit var singleDrinkViewModel: SingleDrinkViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_single_drink)
    singleDrinkViewModel = ViewModelProviders.of(this).get(SingleDrinkViewModel::class.java)
    subscribe()
    single_drink_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
    single_drink_recycler_view.adapter = singleDrinkAdapter
    val drinkId = intent.getIntExtra(Constants.DRINK_ID_PASSED, 0)
    singleDrinkViewModel.getDrink(drinkId)
  }
  
  private fun subscribe() {
    singleDrinkViewModel.drinkMade.observe(this, Observer<Drink> { drinkMade -> drinkMade?.let { setDrinkInformation(it) } })
    singleDrinkViewModel.instructions.observe(this, Observer<Array<out String>> { instructions -> instructions?.let { formatInstructionList(it) } })
  }
  
  private fun setDrinkInformation(drinkMade: Drink) {
    setupToolbar(drinkMade)
    drink_description.text = drinkMade.description
    drink_duration.text = convertBrewDuration(drinkMade.brewDuration)
    drink_strength.text = drinkMade.strength
    drink_difficulty.text = drinkMade.difficulty
    start_timer_fab.setOnClickListener { switchToTimer() }
  }
  
  private fun setupToolbar(drinkMade: Drink) {
    Glide.with(single_drink_image).load(drinkMade.image).into(single_drink_image)
    collapsing_toolbar.title = " "
    setSupportActionBar(single_drink_toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(false)
    supportActionBar?.setDisplayShowHomeEnabled(false)
    
    single_drink_app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
      internal var isShow = false
      internal var scrollRange = -1
      
      override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (scrollRange == -1) {
          scrollRange = appBarLayout.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
          collapsing_toolbar.title = drinkMade.name
          supportActionBar?.setDisplayHomeAsUpEnabled(true)
          supportActionBar?.setDisplayShowHomeEnabled(true)
          isShow = true
        } else if (isShow) {
          collapsing_toolbar.title = " "
          supportActionBar?.setDisplayHomeAsUpEnabled(false)
          supportActionBar?.setDisplayShowHomeEnabled(false)
          isShow = false
        }
      }
    })
  }
  
  private fun formatInstructionList(instructionsFromResources: Array<out String>) {
    val instructionsToBeUsed: ArrayList<String> = ArrayList()
    (0 until instructionsFromResources.size).mapTo(instructionsToBeUsed) { "${it.plus(1)}. " + instructionsFromResources[it] }
    singleDrinkAdapter.setInstructions(instructionsToBeUsed)
    singleDrinkAdapter.notifyDataSetChanged()
  }
  
  private fun convertBrewDuration(originalValue: Int): String {
    return if (originalValue >= 60) {
      val simplifiedTime = originalValue / 60
      simplifiedTime.toString() + ":00 hrs"
    } else {
      originalValue.toString() + ":00 min"
    }
  }
  
  private fun switchToTimer() {
    setResult(Activity.RESULT_OK, Intent())
    finish()
  }
  
  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}