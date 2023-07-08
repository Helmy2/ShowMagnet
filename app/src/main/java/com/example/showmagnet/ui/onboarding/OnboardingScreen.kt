package com.example.showmagnet.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.showmagnet.ui.common.utils.PaletteColor
import com.example.showmagnet.ui.common.utils.PaletteGenerator.extractDomainSwatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onEndOnboarding: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val pagerList by remember {
        mutableStateOf(OnBoardingItems.getData())
    }
    val pagerSize = pagerList.size

    Box {
        PagerField(
            pagerState = pagerState,
            pagerList = pagerList
        )

        BottomField(
            coroutineScope = coroutineScope,
            pagerState = pagerState,
            pagerSize = pagerSize,
            onEndOnboarding = onEndOnboarding,
            pagerList = pagerList,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PagerField(
    pagerState: PagerState,
    pagerList: List<OnBoardingItems>
) {
    HorizontalPager(
        state = pagerState,
        pageCount = pagerList.size,
        beyondBoundsPageCount = 2,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Card(
            Modifier
                .graphicsLayer {
                    val pageOffset =
                        pagerState.calculateCurrentOffsetForPage(page).absoluteValue

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            OnBoardingItem(
                pagerList[page],
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomField(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    pagerSize: Int,
    onEndOnboarding: () -> Unit,
    pagerList: List<OnBoardingItems>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage < pagerSize - 1)
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    else
                        onEndOnboarding()
                }
            },
            contentPadding = PaddingValues(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(text = if (pagerState.currentPage != pagerSize - 1) "Next" else "Start")
        }
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerList.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}


@Composable
private fun OnBoardingItem(
    items: OnBoardingItems,
    modifier: Modifier = Modifier,
) {
    val imageBitmap: ImageBitmap = ImageBitmap.imageResource(items.image)
    var mainColor by remember {
        mutableStateOf(
            PaletteColor(Color.Black, Color.White)
        )
    }

    SideEffect {
        mainColor = extractDomainSwatch(imageBitmap)
    }

    val brushColor: List<Color> =
        listOf(
            Color.Transparent,
            Color.Transparent,
            mainColor.color,
            mainColor.color
        )


    Box(modifier) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight(.7f)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(brushColor),
                )
        )
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(.7f))
            Text(
                text = items.title,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 24.sp,
                color = mainColor.onColor.copy(alpha = .9f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = items.subTitle,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = mainColor.onColor
            )
        }
    }
}

