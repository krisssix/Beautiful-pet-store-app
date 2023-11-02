package cz.mendelu.pef.petstore.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.petstore.ui.theme.Purple40

@Composable
fun ShadowBox() {
    val shadowHeight = 8.dp
    val gradient = Brush.verticalGradient(
        colors = listOf(Purple40.copy(alpha = 0.2f), Color.Transparent)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(shadowHeight)
            .background(gradient)
    )

}