package de.bilkewall.plugins.view.bottomBar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TabBarItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val badgeAmount: Int? = null,
)

@Composable
fun CinderBar(
    tabBarItems: List<TabBarItem>,
    navController: NavController,
) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }
    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            val currentTabBarSelected = tabBarItem.title == navController.currentDestination?.route
            NavigationBarItem(
                selected = currentTabBarSelected,
                onClick = {
                    selectedTabIndex = index
                    if (!currentTabBarSelected) {
                        navController.navigate(tabBarItem.title)
                    }
                },
                icon = {
                    CinderBarIconView(
                        isSelected = currentTabBarSelected,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount,
                    )
                },
            )
        }
    }
}

@Composable
fun CinderBarIconView(
    isSelected: Boolean,
    selectedIcon: Painter,
    unselectedIcon: Painter,
    title: String,
    badgeAmount: Int? = null,
) {
    BadgedBox(badge = { CinderBarBadgeView(badgeAmount) }) {
        Icon(
            painter =
                if (isSelected) {
                    selectedIcon
                } else {
                    unselectedIcon
                },
            modifier = Modifier.size(40.dp),
            contentDescription = title,
        )
    }
}

@Composable
fun CinderBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
