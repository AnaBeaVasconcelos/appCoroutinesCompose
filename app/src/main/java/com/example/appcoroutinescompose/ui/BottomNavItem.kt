
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Coroutines : BottomNavItem(
        route = "coroutines",
        label = "Coroutines",
        icon = Icons.Outlined.Bolt
    )

    object Todo : BottomNavItem(
        route = "todo",
        label = "Todo",
        icon = Icons.AutoMirrored.Outlined.List
    )

    object Form : BottomNavItem(
        route = "form",
        label = "Form",
        icon = Icons.Outlined.Edit
    )

    companion object {
        val items = listOf(Coroutines, Todo, Form)
    }
}