package com.example.obe_result_analysis_mobile_app_1
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.obe_result_analysis_mobile_app_1.databinding.ActivityMainBinding
import com.example.obe_result_analysis_mobile_app_1.ui.gallery.MarksUpdateRequestFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton;
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        // Set the FloatingActionButton listener
        binding.appBarMain.fab.setOnClickListener { view ->
            // Open the new fragment when the button is clicked
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, MarksUpdateRequestFragment())
            transaction.addToBackStack(null) // Allows the user to navigate back
            transaction.commit()
        }

        // Initialize drawer and navigation view
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Set up navigation
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (menu.javaClass.simpleName == "MenuBuilder") {
            try {
                val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}