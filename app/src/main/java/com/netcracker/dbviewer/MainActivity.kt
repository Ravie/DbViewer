package com.netcracker.dbviewer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.netcracker.dbviewer.ui.addresses.AddressListFragment
import com.netcracker.dbviewer.ui.customers.CustomerListFragment
import com.netcracker.dbviewer.ui.hardware.HardwareListFragment
import com.netcracker.dbviewer.ui.services.ServiceListFragment


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_customer_list,
                R.id.nav_service_list,
                R.id.nav_hardware_list,
                R.id.nav_address_list
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { item -> onNavigationItemSelected(item) }
    }


    private fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Создадим новый фрагмент
        var fragment: Fragment? = null

        // Handle navigation view item clicks here.
        try {
            when (item.itemId) {
                R.id.nav_customer_button -> {
                    fragment = CustomerListFragment.newInstance()
                }
                R.id.nav_service_button -> {
                    fragment = ServiceListFragment.newInstance()
                }
                R.id.nav_hardware_button -> {
                    fragment = HardwareListFragment.newInstance()
                }
                R.id.nav_address_button -> {
                    fragment = AddressListFragment.newInstance()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
        }
        // Выделяем выбранный пункт меню в шторке
        item.isChecked = true
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
