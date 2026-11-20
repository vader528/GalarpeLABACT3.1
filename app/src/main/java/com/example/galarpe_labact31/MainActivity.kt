package com.example.galarpe_labact31

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        styleSystemBars()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_account,
                R.id.nav_profile,
                R.id.nav_mysports,
            ),
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<BottomNavigationView>(R.id.nav_view)
            ?.setupWithNavController(navController)
    }

    private fun styleSystemBars() {
        val decor = window.decorView
        val statusBar = ColorDrawable(getColor(R.color.purple_700))
        val navigationBar = ColorDrawable(getColor(R.color.black))
        decor.overlay.add(statusBar)
        decor.overlay.add(navigationBar)

        WindowCompat.getInsetsController(window, decor).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        fun updateBarBounds() {
            val insets = ViewCompat.getRootWindowInsets(decor) ?: return
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigation = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            statusBar.setBounds(0, 0, decor.width, status.top)
            when {
                navigation.bottom > 0 -> navigationBar.setBounds(
                    0, decor.height - navigation.bottom, decor.width, decor.height,
                )
                navigation.left > 0 -> navigationBar.setBounds(
                    0, 0, navigation.left, decor.height,
                )
                navigation.right > 0 -> navigationBar.setBounds(
                    decor.width - navigation.right, 0, decor.width, decor.height,
                )
                else -> navigationBar.setBounds(0, 0, 0, 0)
            }
        }

        val container = findViewById<android.view.View>(R.id.container)
        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            updateBarBounds()
            ViewCompat.onApplyWindowInsets(view, insets)
        }
        decor.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateBarBounds() }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
