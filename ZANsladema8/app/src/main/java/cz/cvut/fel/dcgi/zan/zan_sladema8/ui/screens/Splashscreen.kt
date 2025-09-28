package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.cvut.fel.dcgi.zan.zan_sladema8.R
import kotlinx.coroutines.delay

@Composable
fun Splashscreen(
    onNavigate: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.splashscreen),
            contentDescription = "Splashscreen",
            modifier = Modifier.fillMaxSize()
        )
        Text (
            "Home Workout",
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 116.dp)
                .align(Alignment.TopCenter)

        )
    }
    LaunchedEffect(Unit) {
        delay(1000L)
        onNavigate()
    }
}