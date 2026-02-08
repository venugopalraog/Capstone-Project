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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.designpatterntutorial.viewmodels.FavoritesState
import com.capstone.designpatterntutorial.viewmodels.HomeEvent
import com.capstone.designpatterntutorial.viewmodels.HomeState
import com.capstone.designpatterntutorial.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

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
                    val homeState by viewModel.state.collectAsState()
                    val favoritesState by viewModel.favoritesState.collectAsState()

                    NavHost(navController = navController, startDestination = "categories") {
                        composable("categories") {
                             when (val state = homeState) {
                                is HomeState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                is HomeState.Success -> {
                                    CategoryList(categories = state.data.categoryList) {
                                        navController.navigate("pattern/${it.id}")
                                    }
                                }
                                is HomeState.Error -> {
                                    Text(text = state.message)
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
                                        navController.navigate("pattern/${it.id}")
                                    }
                                }
                                is FavoritesState.Error -> {
                                    Text(text = favState.message)
                                }
                            }
                        }
                        composable(
                            "pattern/{patternId}",
                            arguments = listOf(navArgument("patternId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val patternId = backStackEntry.arguments?.getInt("patternId")
                            if (patternId != null) {
                                // Find the pattern from the state
                                val pattern = (homeState as? HomeState.Success)?.data?.categoryList?.flatMap { it.patternList }?.find { it.id == patternId }
                                    ?: (favoritesState as? FavoritesState.Success)?.patterns?.find { it.id == patternId }

                                if (pattern != null) {
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
                }
            }
        )
    }
}
