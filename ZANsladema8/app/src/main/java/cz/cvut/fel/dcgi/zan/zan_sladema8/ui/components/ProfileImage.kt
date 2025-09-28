package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cz.cvut.fel.dcgi.zan.zan_sladema8.R

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.profile),
        contentDescription = "User profile icon",
        modifier = modifier
            .padding(top = 24.dp)
            .height(250.dp)
    )
}