package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.BottomNavigationItem

@Composable
fun MainBottomNavigation(
    mainBottomNavigationItems: List<BottomNavigationItem>,
    currentRoute: String?,
) {
    NavigationBar {
        mainBottomNavigationItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.iconId),
                        contentDescription = item.contentDescription,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.label) },
                selected = item.route == currentRoute,
                onClick = item.onClick,
            )
        }
    }
}