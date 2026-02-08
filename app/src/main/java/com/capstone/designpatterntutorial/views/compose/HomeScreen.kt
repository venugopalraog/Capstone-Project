package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    val homeState by viewModel.state.collectAsState()
    val favoritesState by viewModel.favoritesState.collectAsState()
    var selectedCategoryIndex by remember { mutableStateOf(0) }

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
                    title = { 
                        val currentRoute = navController.currentBackStackEntry?.destination?.route
                        Text(if (currentRoute == "favorites") "Favorites" else "Design Pattern Tutorial") 
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                if (homeState is HomeState.Success) {
                    val categories = (homeState as HomeState.Success).data.categoryList
                    NavigationBar {
                        categories.forEachIndexed { index, category ->
                            val icon = when (category.name) {
                                "Creational" -> Icons.Filled.Add
                                "Structural" -> Icons.Filled.AccountTree
                                "Behavioral" -> Icons.Filled.Sync
                                else -> Icons.Filled.Menu
                            }
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = category.name) },
                                label = { Text(category.name) },
                                selected = selectedCategoryIndex == index,
                                onClick = { selectedCategoryIndex = index }
                            )
                        }
                    }
                }
            },
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                             when (val state = homeState) {
                                is HomeState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                is HomeState.Success -> {
                                    val category = state.data.categoryList[selectedCategoryIndex]
                                    CategoryScreen(category = category) {
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
                                    if (favState.patterns.isEmpty()) {
                                        Text("You haven't added any favorites yet.")
                                    } else {
                                        FavoriteList(patterns = favState.patterns) {
                                            navController.navigate("pattern/${it.id}")
                                        }
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
