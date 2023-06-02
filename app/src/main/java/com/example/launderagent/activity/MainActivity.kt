package com.example.launderagent.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.launderagent.ui.auth.navigation.AppNavHost
import com.example.launderagent.data.MainViewModel
import com.example.launderagent.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavHost(viewModel)
            }
        }
    }

}