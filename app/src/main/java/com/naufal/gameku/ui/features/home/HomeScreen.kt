import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naufal.gameku.data.game.model.response.GamesResponse
import com.naufal.gameku.ui.components.CustomCoilImage
import com.naufal.gameku.ui.components.shimmerEffect
import com.naufal.gameku.ui.features.home.HomeViewModel
import com.naufal.gameku.ui.theme.GamekuTheme
import com.naufal.gameku.ui.util.toStringGenres
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    openGameDetail: (Int) -> Unit = {},
    openFavoriteScreen: () -> Unit = {},
) {
    val homeState by viewModel.homeState.collectAsState()

    HomeScreenContent(
        homeState = homeState,
        openGameDetail = openGameDetail,
        openFavoriteScreen = openFavoriteScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    homeState: HomeViewModel.HomeState = HomeViewModel.HomeState(),
    openGameDetail: (Int) -> Unit = {},
    openFavoriteScreen: () -> Unit = {},
) {
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RAWG GAMES",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = { openFavoriteScreen() }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "favorite",
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
                if (homeState.games?.isNotEmpty() == true) {
                    val games = homeState.games
                    items(games.size) { index ->
                        val gameDetail: GamesResponse.Result? = games[index]
                        gameDetail?.let {
                            ItemGame(gameDetail = it, openGameDetail = openGameDetail)
                        }
                    }
                } else {
                    items(5) {
                        ItemGameShimmer()
                    }
                }
            }

            if (homeState.error == true) {
                LaunchedEffect(snackbarHostState) {
                    snackScope.launch {
                        snackbarHostState.showSnackbar(
                            homeState.message ?: "Gagal memuat data"
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
    gameDetail: GamesResponse.Result,
    openGameDetail: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                gameDetail.id?.let { openGameDetail(it) }
            },
    ) {
        CustomCoilImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            model = gameDetail.backgroundImage ?: "",
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = gameDetail.name ?: "-",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Release Date: ${gameDetail.released ?: "-"}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Genres: ${gameDetail.genres.toStringGenres()}",
            style = MaterialTheme.typography.labelMedium,
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
fun HomeScreenPreview() {
    GamekuTheme {
        Surface {
            HomeScreenContent(
                homeState = HomeViewModel.HomeState(
                    games = listOf(
                        GamesResponse.Result(
                            name = "Namanamanama", released = "23-09-2019",
                            genres = listOf(
                                GamesResponse.Result.Genre(name = "RPG"),
                                GamesResponse.Result.Genre(name = "Action"),
                            ),
                        ),

                        GamesResponse.Result(
                            name = "asdasdasdasdsad", released = "23-09-2019",
                            genres = listOf(
                                GamesResponse.Result.Genre(name = "RPG"),
                                GamesResponse.Result.Genre(name = "Action"),
                            ),
                        ),
                    )
                )
            )
        }
    }
}