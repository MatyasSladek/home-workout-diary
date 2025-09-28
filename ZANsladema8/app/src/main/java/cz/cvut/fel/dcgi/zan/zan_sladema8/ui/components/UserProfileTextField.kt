package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun UserProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onTrailingIconClick: () -> Unit = { onValueChange("") },
    label: String = "",
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        trailingIcon = {
            IconButton(
                onClick = onTrailingIconClick
            ) {
                Icon(
                    icon,
                    contentDescription = "Clear text",
                )
            }
        },
    )
}