package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.viewmodels.FavoritesState
import com.capstone.designpatterntutorial.viewmodels.HomeEvent
import com.capstone.designpatterntutorial.viewmodels.HomeState
import com.capstone.designpatterntutorial.viewmodels.HomeViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawer(onFavoritesClicked = {
                    viewModel.onEvent(HomeEvent.LoadFavorites)
                    navController.navigate("favorites")
                    scope.launch { drawerState.close() }
                })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Design Pattern Tutorial") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    val state by viewModel.state.collectAsState()
                    val favoritesState by viewModel.favoritesState.collectAsState()

                    NavHost(navController = navController, startDestination = "categories") {
                        composable("categories") {
                             when (val homeState = state) {
                                is HomeState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                is HomeState.Success -> {
                                    CategoryList(categories = homeState.data.categoryList) {
                                        val patternJson = Gson().toJson(it)
                                        val encodedPattern = URLEncoder.encode(patternJson, StandardCharsets.UTF_8.toString())
                                        navController.navigate("pattern/$encodedPattern")
                                    }
                                }
                                is HomeState.Error -> {
                                    Text(text = homeState.message)
                                }
                            }
                        }
                        composable("favorites") {
                            when (val favState = favoritesState) {
                                is FavoritesState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                is FavoritesState.Success -> {
                                    FavoriteList(patterns = favState.patterns) {
                                        val patternJson = Gson().toJson(it)
                                        val encodedPattern = URLEncoder.encode(patternJson, StandardCharsets.UTF_8.toString())
                                        navController.navigate("pattern/$encodedPattern")
                                    }
                                }
                                is FavoritesState.Error -> {
                                    Text(text = favState.message)
                                }
                            }
                        }
                        composable(
                            "pattern/{pattern}",
                            arguments = listOf(navArgument("pattern") { type = PatternNavType })
                        ) { backStackEntry ->
                            val pattern = backStackEntry.arguments?.getSerializable("pattern") as Pattern
                            PatternScreen(
                                pattern = pattern,
                                onBackClicked = { navController.popBackStack() },
                                onFavoriteClicked = { 
                                    viewModel.onEvent(HomeEvent.ToggleFavorite(it))
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}
