package com.jonathanl.bgmanager

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jonathanl.bgmanager.base.BaseActivity
import com.jonathanl.bgmanager.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.app_bar_main.view.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var mainActivityVM: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.drawerLayout.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)

        setUpDI()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_search,
                R.id.nav_game_list,
                R.id.nav_tools,
                R.id.nav_settings,
                R.id.nav_share
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    // to enable the toolbar to support Up navigation
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setUpDI() {
        getMainActivityComponent().inject(this)
    }

}