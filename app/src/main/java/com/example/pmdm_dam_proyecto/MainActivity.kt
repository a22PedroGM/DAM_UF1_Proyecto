package com.example.pmdm_dam_proyecto

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),OnMenuClickListener {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val builderApp = AppBarConfiguration.Builder(navController.graph)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            onMenuClick(menuItem.itemId)
            drawerLayout.closeDrawers()
            true
        }

        builderApp.setOpenableLayout(drawerLayout)
        appBarConfiguration = builderApp.build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }
    override fun onMenuClick(id: Int) {
        when (id) {
            R.id.action_red -> navigateToGameFragment(R.drawable.ball_image)
            R.id.action_blue -> navigateToGameFragment(R.drawable.ball_imageblue)
            R.id.action_green -> navigateToGameFragment(R.drawable.ball_imagegreen)
        }
    }
    private fun navigateToGameFragment(imageResId: Int) {
        val bundle = Bundle().apply {
            putInt("imageResId", imageResId)
        }
        navController.navigate(R.id.gameFragment, bundle)
    }
}
