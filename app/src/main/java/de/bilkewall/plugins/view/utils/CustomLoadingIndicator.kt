package de.bilkewall.plugins.view.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.bilkewall.cinder.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomLoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.glass),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.Unspecified,
        )

        CustomRotatingAnimation()
    }
}

@Composable
fun CustomRotatingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Animation for rotation angle
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "",
    )

    // Calculating circular position
    val radius = 80.dp
    val offsetX = radius * cos(Math.toRadians(angle.toDouble())).toFloat()
    val offsetY = radius * sin(Math.toRadians(angle.toDouble())).toFloat()

    // Positioning cherry around the center
    Icon(
        painter = painterResource(id = R.drawable.cherry),
        contentDescription = null,
        modifier =
            Modifier
                .size(40.dp)
                .offset(x = offsetX, y = offsetY),
        tint = Color.Unspecified,
    )
}
