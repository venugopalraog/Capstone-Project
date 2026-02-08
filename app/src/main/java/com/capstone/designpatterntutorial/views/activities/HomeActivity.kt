package com.capstone.designpatterntutorial.views.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.capstone.designpatterntutorial.di.MyApplication
import com.capstone.designpatterntutorial.viewmodels.HomeEvent
import com.capstone.designpatterntutorial.viewmodels.HomeViewModel
import com.capstone.designpatterntutorial.views.compose.HomeScreen
import javax.inject.Inject

class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        viewModel.onEvent(HomeEvent.LoadPatterns)

        setContent {
            HomeScreen(viewModel)
        }
    }
}
