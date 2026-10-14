package com.dungtien.amthucbavi.member1_auth_home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.databinding.ActivityMainBinding
import com.dungtien.amthucbavi.member2_catalog.CategoriesFragment
import com.dungtien.amthucbavi.member2_catalog.MenuBoardFragment
import com.dungtien.amthucbavi.member5_planner_profile.MealPlannerFragment
import com.dungtien.amthucbavi.member5_planner_profile.ProfileFragment
import com.dungtien.amthucbavi.services.AuthService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = AuthService.getInstance(this).currentUser
        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_categories -> CategoriesFragment()
                R.id.nav_menu -> MenuBoardFragment()
                R.id.nav_planner -> MealPlannerFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    fun selectTab(itemId: Int) {
        binding.bottomNav.selectedItemId = itemId
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
