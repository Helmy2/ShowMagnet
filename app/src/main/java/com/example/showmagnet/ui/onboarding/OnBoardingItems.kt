package com.example.showmagnet.ui.onboarding

import androidx.annotation.DrawableRes
import com.example.showmagnet.R

class OnBoardingItems(
    val title: String,
    val subTitle: String,
    @DrawableRes val image: Int,
) {
    companion object {
        fun getData(): List<OnBoardingItems> {
            return listOf(
                OnBoardingItems(
                    "Welcome to ShowMagnet!",
                    "Your ultimate guide to the world of movies and TV shows.",
                    R.drawable.image_1
                ),
                OnBoardingItems(
                    "Discover & Explore",
                    "Stay updated with the latest releases and explore a vast library of movies and TV shows.",
                    R.drawable.image_2
                ),
                OnBoardingItems(
                    "Trending & Popular",
                    "Get recommendations for trending and popular content. Never miss out on what's hot.",
                    R.drawable.image_3
                ), OnBoardingItems(
                    "Personalize Your Experience",
                    "Create your own watchlist and get recommendations tailored to your taste. Dive into the world of entertainment now!",
                    R.drawable.image_4
                )
            )
        }
    }
}