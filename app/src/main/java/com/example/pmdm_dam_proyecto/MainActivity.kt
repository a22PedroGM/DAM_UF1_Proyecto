package com.example.pmdm_dam_proyecto

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout =
            findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val builderApp = AppBarConfiguration.Builder(navController.graph)
        builderApp.setOpenableLayout(drawerLayout)

    }
}

