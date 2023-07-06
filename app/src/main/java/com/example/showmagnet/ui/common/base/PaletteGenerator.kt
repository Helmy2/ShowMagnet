package com.example.showmagnet.ui.common.base

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.palette.graphics.Palette

data class PaletteColor(val color: Color, val onColor: Color)

object PaletteGenerator {

    fun extractDomainSwatch(bitmap: ImageBitmap): PaletteColor {
        val palette = Palette.from(bitmap.asAndroidBitmap()).generate().dominantSwatch
        val color = parseColor(parseBodyColor(palette?.rgb) ?: "#000000")
        val onColor = parseColor(parseBodyColor(palette?.bodyTextColor) ?: "#000000")
        return PaletteColor(Color(color), Color(onColor))
    }

    private fun parseBodyColor(color: Int?): String? {
        return if (color != null) {
            val parsedColor = Integer.toHexString(color)
            "#$parsedColor"
        } else {
            null
        }
    }
}