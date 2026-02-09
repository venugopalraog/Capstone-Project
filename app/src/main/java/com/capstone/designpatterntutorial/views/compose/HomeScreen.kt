package com.capstone.designpatterntutorial.views.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.viewmodels.FavoritesState
import com.capstone.designpatterntutorial.viewmodels.HomeEvent
import com.capstone.designpatterntutorial.viewmodels.HomeState
import com.capstone.designpatterntutorial.viewmodels.HomeViewModel
import com.capstone.designpatterntutorial.viewmodels.RecentsState
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Home : Screen("home", "Home", { Icon(Icons.Filled.Home, null) })
    object Favorites : Screen("favorites", "Favorites", { Icon(Icons.Filled.Favorite, null) })
    object Recents : Screen("recents", "Recents", { Icon(Icons.Filled.Sync, null) })
}

val bottomNavItems = listOf(Screen.Home, Screen.Favorites, Screen.Recents)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val navController = rememberNavController()
    val homeState by viewModel.state.collectAsState()
    val favoritesState by viewModel.favoritesState.collectAsState()
    val recentsState by viewModel.recentsState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val showMainUI = currentRoute in bottomNavItems.map { it.route }

    if (isSearchActive) {
        BackHandler {
            isSearchActive = false
            searchQuery = ""
        }
    }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.onEvent(HomeEvent.Search(it))
                            },
                            placeholder = { Text("Search Patterns") },
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearchActive = false
                            searchQuery = ""
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close Search")
                        }
                    }
                )
            } else {
                when {
                    currentRoute?.startsWith("pattern/") == true -> {
                        val patternId = navBackStackEntry?.arguments?.getInt("patternId")
                        val pattern = patternId?.let { findPatternById(it, homeState, favoritesState, recentsState, searchState) }
                        TopAppBar(
                            title = { Text(pattern?.name ?: "Pattern Detail") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                            actions = {
                                pattern?.let { p ->
                                    IconButton(onClick = { viewModel.onEvent(HomeEvent.ToggleFavorite(p)) }) {
                                        Icon(
                                            if (p.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                            contentDescription = "Favorite"
                                        )
                                    }
                                }
                            }
                        )
                    }
                    currentRoute == "about" -> {
                        TopAppBar(
                            title = { Text("About") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        )
                    }
                    else -> {
                        var showMenu by remember { mutableStateOf(false) }
                        TopAppBar(
                            title = { Text("Design Pattern Tutorial") },
                            actions = {
                                IconButton(onClick = { isSearchActive = true }) {
                                    Icon(Icons.Filled.Search, contentDescription = "Search")
                                }
                                IconButton(onClick = { showMenu = !showMenu }) {
                                    Icon(Icons.Filled.MoreVert, contentDescription = "More")
                                }
                                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                    DropdownMenuItem(
                                        text = { Text("About") },
                                        onClick = {
                                            navController.navigate("about")
                                            showMenu = false
                                        },
                                        leadingIcon = { Icon(Icons.Filled.Info, null) }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (showMainUI && !isSearchActive) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon() },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isSearchActive) {
                SearchScreen(searchState = searchState, query = searchQuery) {
                    isSearchActive = false
                    navController.navigate("pattern/${it.id}")
                }
            } else {
                NavHost(navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        when (val state = homeState) {
                            is HomeState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                            is HomeState.Success -> {
                                val pagerState = rememberPagerState(pageCount = { state.data.categoryList.size })
                                val scope = rememberCoroutineScope()
                                Column {
                                    TabRow(selectedTabIndex = pagerState.currentPage) {
                                        state.data.categoryList.forEachIndexed { index, category ->
                                            Tab(
                                                text = { Text(category.name) },
                                                selected = pagerState.currentPage == index,
                                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                                            )
                                        }
                                    }
                                    HorizontalPager(state = pagerState) {
                                        val category = state.data.categoryList[it]
                                        CategoryScreen(category = category) {
                                            viewModel.onEvent(HomeEvent.AddRecent(it))
                                            navController.navigate("pattern/${it.id}")
                                        }
                                    }
                                }
                            }
                            is HomeState.Error -> Text(text = state.message)
                        }
                    }
                    composable(Screen.Favorites.route) {
                        LaunchedEffect(Unit) { viewModel.onEvent(HomeEvent.LoadFavorites) }
                        when (val favState = favoritesState) {
                            is FavoritesState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                            is FavoritesState.Success -> {
                                if (favState.patterns.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text("You haven't added any favorites yet.")
                                    }
                                } else {
                                    FavoriteList(patterns = favState.patterns) {
                                        viewModel.onEvent(HomeEvent.AddRecent(it))
                                        navController.navigate("pattern/${it.id}")
                                    }
                                }
                            }
                            is FavoritesState.Error -> Text(text = favState.message)
                        }
                    }
                    composable(Screen.Recents.route) {
                        LaunchedEffect(Unit) { viewModel.onEvent(HomeEvent.LoadRecents) }
                        when (val recentState = recentsState) {
                            is RecentsState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                            is RecentsState.Success -> {
                                if (recentState.patterns.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text("You haven't viewed any patterns yet.")
                                    }
                                } else {
                                    FavoriteList(patterns = recentState.patterns) {
                                        viewModel.onEvent(HomeEvent.AddRecent(it))
                                        navController.navigate("pattern/${it.id}")
                                    }
                                }
                            }
                            is RecentsState.Error -> Text(text = recentState.message)
                        }
                    }
                    composable("about") {
                        AboutScreen()
                    }
                    composable(
                        "pattern/{patternId}",
                        arguments = listOf(navArgument("patternId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val patternId = backStackEntry.arguments?.getInt("patternId")
                        if (patternId != null) {
                            val pattern = findPatternById(patternId, homeState, favoritesState, recentsState, searchState)
                            if (pattern != null) {
                                PatternScreen(pattern = pattern)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun findPatternById(patternId: Int, vararg states: Any): Pattern? {
    for (state in states) {
        val foundPattern = when (state) {
            is HomeState.Success -> state.data.categoryList.flatMap { it.patternList }.find { it.id == patternId }
            is FavoritesState.Success -> state.patterns.find { it.id == patternId }
            is RecentsState.Success -> state.patterns.find { it.id == patternId }
            is com.capstone.designpatterntutorial.viewmodels.SearchState.Success -> state.patterns.find { it.id == patternId }
            else -> null
        }
        if (foundPattern != null) return foundPattern
    }
    return null
}
