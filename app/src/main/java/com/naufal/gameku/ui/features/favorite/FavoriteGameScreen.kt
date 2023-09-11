package com.naufal.gameku.ui.features.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.naufal.gameku.data.game.local.model.GameEntity
import com.naufal.gameku.ui.components.CustomCoilImage
import com.naufal.gameku.ui.components.shimmerEffect
import com.naufal.gameku.ui.theme.GamekuTheme
import com.naufal.gameku.ui.util.OnLifecycleEvent
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.launch

@Composable
fun FavoriteGameScreen(
    viewModel: FavoriteGameViewModel = hiltViewModel(),
    openGameDetailScreen: (Int) -> Unit = {},
    openHomeScreen: () -> Unit = {},
) {
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.getGames()
            }

            else -> {}
        }
    }

    val favoriteGameState by viewModel.favoriteGameState.collectAsState()

    FavoriteGameScreenContent(
        favoriteGameState = favoriteGameState,
        openGameDetailScreen = openGameDetailScreen,
        openHomeScreen = openHomeScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteGameScreenContent(
    favoriteGameState: FavoriteGameViewModel.FavoriteGameState = FavoriteGameViewModel.FavoriteGameState(),
    openGameDetailScreen: (Int) -> Unit = {},
    openHomeScreen: () -> Unit = {},
) {
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite Games",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = { openHomeScreen() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp, top = 24.dp),
            ) {
                if (favoriteGameState.games?.isNotEmpty() == true) {
                    val games = favoriteGameState.games
                    items(games.size) { index ->
                        val gameEntity: GameEntity = games[index]
                        gameEntity.let {
                            ItemGame(gameEntity = it, openGameDetailScreen = openGameDetailScreen)
                        }
                    }
                } else {
                    items(3) {
                        ItemGameShimmer()
                    }
                }
            }

            if (favoriteGameState.error == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            favoriteGameState.message ?: "Failed load data"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemGame(
    modifier: Modifier = Modifier,
    gameEntity: GameEntity,
    openGameDetailScreen: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                gameEntity.id?.let { openGameDetailScreen(it) }
            },
    ) {
        CustomCoilImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            model = gameEntity.backgroundImage ?: "",
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = gameEntity.name ?: "-",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Release Date: ${gameEntity.released ?: "-"}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Genres: ${gameEntity.genres}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ItemGameShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(30.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(20.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(20.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun FavoriteGameScreenPreview() {
    GamekuTheme {
        Surface {
            FavoriteGameScreenContent()
        }
    }
}