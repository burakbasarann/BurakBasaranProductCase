package com.basaran.burakbasarancase.view.activities


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.basaran.burakbasarancase.R
import com.basaran.burakbasarancase.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createBottomNavigation()

    }

    private fun createBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHosFragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )
    }

    @SuppressLint("RestrictedApi")
    fun updateBasketBadgeCount(count: Int) {
        val menuView = binding.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(1) as BottomNavigationItemView
        val badge = binding.bottomNavigationView.getOrCreateBadge(itemView.id)
        badge.number = count
    }
    @SuppressLint("RestrictedApi")
    fun updateFavBadgeCount(count: Int) {
        val menuView = binding.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
        val badge = binding.bottomNavigationView.getOrCreateBadge(itemView.id)
        badge.number = count
    }



}


