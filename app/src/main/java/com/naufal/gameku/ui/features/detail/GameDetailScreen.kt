import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.naufal.gameku.R
import com.naufal.core.domain.game.model.GameDetail
import com.naufal.gameku.ui.components.CustomCoilImage
import com.naufal.gameku.ui.components.shimmerEffect
import com.naufal.gameku.ui.features.detail.GameDetailViewModel
import com.naufal.gameku.ui.theme.GamekuTheme
import com.naufal.gameku.ui.util.gameDetailToStringGenres
import com.naufal.gameku.ui.util.toStringDevelopers
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.launch

@Composable
fun GameDetailScreen(
    viewModel: GameDetailViewModel = hiltViewModel(),
    gameId: Int = 0,
    openHomeScreen: () -> Unit = {},
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getGameDetail(gameId)
    }

    val gameDetailState by viewModel.gameDetailState.collectAsState()
    val favoriteGameState by viewModel.favoriteGameState.collectAsState()
    val saveFavoriteGameState by viewModel.saveFavoriteGameState.collectAsState()

    if (gameDetailState.gameDetail != null) {
        LaunchedEffect(key1 = Unit) {
            viewModel.checkFavoriteGame(gameId)
        }
    }

    GameDetailScreenContent(
        gameDetailState = gameDetailState,
        favoriteGameState = favoriteGameState,
        saveFavoriteGameState = saveFavoriteGameState,
        openHomeScreen = openHomeScreen,
        onFavClick = {
            viewModel.saveFavoriteGame(it)
        },
        onUnFavClick = {
            it.id?.let { it1 -> viewModel.unFavoriteGame(it1) }
        },
    )
}

@Composable
fun GameDetailScreenContent(
    gameDetailState: GameDetailViewModel.GameDetailState = GameDetailViewModel.GameDetailState(),
    favoriteGameState: GameDetailViewModel.FavoriteGameState = GameDetailViewModel.FavoriteGameState(),
    saveFavoriteGameState: GameDetailViewModel.SaveFavoriteGameState = GameDetailViewModel.SaveFavoriteGameState(),
    openHomeScreen: () -> Unit = {},
    onFavClick: (GameDetail) -> Unit = {},
    onUnFavClick: (GameDetail) -> Unit = {},
) {
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (gameDetailState.loading == true) {
                GameDetailShimmer()
            } else if (gameDetailState.gameDetail != null) {
                val gameDetail = gameDetailState.gameDetail
                val gameDesc = HtmlCompat.fromHtml(gameDetail.description.toString(), 0)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Box {
                        CustomCoilImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            model = gameDetail.backgroundImage ?: "",
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            IconButton(
                                onClick = { openHomeScreen() },
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(6.dp),
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }

                            if (favoriteGameState.loading == true || saveFavoriteGameState.loading == true) {
                                Box(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .shimmerEffect()
                                )
                            } else {
                                if (favoriteGameState.isFavorite == true) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(6.dp)
                                            .clickable {
                                                onUnFavClick(gameDetail)
                                            },
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "favorite",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                } else {
                                    Icon(
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(6.dp)
                                            .clickable {
                                                onFavClick(gameDetail)
                                            },
                                        imageVector = Icons.Filled.FavoriteBorder,
                                        contentDescription = "favorite",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = gameDetail.name ?: "-",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Release Date: ${gameDetail.released ?: "-"}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Genres: ${gameDetail.genres.gameDetailToStringGenres()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Developer: ${gameDetail.developers.toStringDevelopers()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Description",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AndroidView(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        factory = {
                            val font = ResourcesCompat.getFont(it, R.font.inter_regular)

                            TextView(it).apply {
                                textSize = 12F
                                typeface = font
                            }
                        },
                        update = { it.text = gameDesc },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            if (gameDetailState.error == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            gameDetailState.message ?: "Failed load data"
                        )
                    }
                }
            }

            if (saveFavoriteGameState.error == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            "Failed save to favorite game"
                        )
                    }
                }
            }

            if (saveFavoriteGameState.success == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            "Success save to favorite game"
                        )
                    }
                }
            }

            if (saveFavoriteGameState.errorUnFav == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            "Failed remove from favorite game"
                        )
                    }
                }
            }

            if (saveFavoriteGameState.successUnFav == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            "Success remove from favorite game"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameDetailShimmer(
    openHomeScreen: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .shimmerEffect()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = { openHomeScreen() },
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(6.dp),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(20.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Description",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(16.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(16.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(16.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameDetailScreenPreview() {
    GamekuTheme {
        Surface {
            GameDetailScreenContent(
                gameDetailState = GameDetailViewModel.GameDetailState(
                    gameDetail = GameDetail(
                        name = "Namanamanama", released = "23-09-2019",
                        genres = listOf(
                            GameDetail.Genre(name = "RPG"),
                            GameDetail.Genre(name = "Action"),
                        ),
                        description = "asdsadsad",
                        developers = listOf(
                            GameDetail.Developer(name = "Ubisoft"),
                            GameDetail.Developer(name = "Toge"),
                        ),
                    )
                )
            )
        }
    }
}