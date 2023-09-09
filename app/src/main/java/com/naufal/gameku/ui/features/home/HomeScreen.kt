import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.naufal.gameku.data.game.model.response.GamesResponse
import com.naufal.gameku.ui.components.CustomCoilImage
import com.naufal.gameku.ui.components.CustomOutlinedTextField
import com.naufal.gameku.ui.components.shimmerEffect
import com.naufal.gameku.ui.features.home.HomeViewModel
import com.naufal.gameku.ui.theme.GamekuTheme
import com.naufal.gameku.ui.util.toStringGenres
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    openGameDetail: (Int) -> Unit = {},
    openFavoriteScreen: () -> Unit = {},
) {
    val gamePagingItems: LazyPagingItems<GamesResponse.Result> =
        viewModel.gamesState.collectAsLazyPagingItems()

    HomeScreenContent(
        gamePagingItems = gamePagingItems,
        onTextChanged = { text ->
            viewModel.getGames(text)
        },
        openGameDetail = openGameDetail,
        openFavoriteScreen = openFavoriteScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    gamePagingItems: LazyPagingItems<GamesResponse.Result>,
    onTextChanged: (String) -> Unit = {},
    openGameDetail: (Int) -> Unit = {},
    openFavoriteScreen: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var search by rememberSaveable { mutableStateOf("") }

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
                item {
                    SearchField(search) {
                        search = it
                        onTextChanged(it)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(gamePagingItems.itemCount) { index ->
                    gamePagingItems[index]?.let {
                        ItemGame(
                            gameDetail = it,
                            openGameDetail = { id ->
                                openGameDetail(id)
                            }
                        )
                    }
                }
                gamePagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            items(5) { ItemGameShimmer() }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = gamePagingItems.loadState.refresh as LoadState.Error
                            Log.d("HomeScreen", "error: ${error.error.localizedMessage}")
                            item {
                                ErrorMessage(
                                    modifier = Modifier.fillParentMaxSize(),
                                    message = "error, try again",
                                    onClickRetry = { retry() },
                                )
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item { ItemGameShimmer() }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = gamePagingItems.loadState.append as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier,
                                    message = error.error.localizedMessage ?: "error",
                                    onClickRetry = { retry() },
                                )
                            }
                        }
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

@Composable
fun SearchField(
    text: String,
    onTextChanged: (String) -> Unit,
) {
    CustomOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = text,
        onValueChanged = {
            onTextChanged(it)
        },
        placeholder = {
            Text(
                text = "Search Games",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
    )
}

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Retry")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    GamekuTheme {
        Surface {
            val listGames = listOf(
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

            HomeScreenContent(
                gamePagingItems = flowOf(PagingData.from(listGames)).collectAsLazyPagingItems()
            )
        }
    }
}