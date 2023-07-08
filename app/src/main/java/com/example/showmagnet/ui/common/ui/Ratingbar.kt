package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Ratingbar(
    rating: Float,
    modifier: Modifier,
) {
    var currentRating = rating / 2
    Box(modifier = modifier.aspectRatio(5f)) {
        Row {
            for (i in 0 until 5) {
                Box(modifier = Modifier.weight(1f)) {
                    DrawStar(
                        modifier = Modifier
                            .fillMaxHeight(), color = Color.Gray
                    )
                    if (currentRating > 0) {
                        if (currentRating >= 1)
                            DrawStar(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                color = Color.Yellow
                            )
                        if (currentRating >= .5 && currentRating < 1)
                            DrawStar(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                color = Color.Yellow,
                                halfStar = true
                            )
                        currentRating -= 1
                    }

                }
            }
        }
    }
}

@Composable
private fun DrawStar(color: Color, modifier: Modifier = Modifier, halfStar: Boolean = false) {
    Canvas(
        modifier = modifier
    ) {
        val path = starPath(size.height, halfStar = halfStar)
        drawPath(path = path, color = color)
    }
}

private fun starPath(starSize: Float, halfStar: Boolean): Path {
    val path = Path()

    path.moveTo(0f, starSize * .37f)
    path.lineTo(starSize * 0.37f, starSize * 0.37f)
    path.lineTo(starSize * .5f, 0f)
    if (!halfStar) {
        path.lineTo(starSize * 0.63f, starSize * 0.37f)
        path.lineTo(starSize, starSize * 0.373f)
        path.lineTo(starSize * 0.72f, starSize * 0.62f)
        path.lineTo(starSize * .81f, starSize)
    }
    path.lineTo(starSize * .50f, starSize * .77f)
    path.lineTo(starSize * .2f, starSize)
    path.lineTo(starSize * .28f, starSize * .62f)

    return path
}

@Composable
@Preview
private fun RatingbarPreview() {
    Ratingbar(rating = 3f, Modifier.height(100.dp))
}