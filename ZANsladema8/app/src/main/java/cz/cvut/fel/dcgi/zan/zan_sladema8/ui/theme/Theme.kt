package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WorkoutDarkColorScheme = darkColorScheme(
    primary = Color(0xFF0B54B4),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF054569),
    onPrimaryContainer = Color(0xFFD4FAFF),

    secondary = Color(0xFF4FC3F7),
    onSecondary = Color(0xFF001F2A),
    secondaryContainer = Color(0xFF004B61),
    onSecondaryContainer = Color(0xFFB8E6FF),

    tertiary = Color(0xFFBA68C8),
    onTertiary = Color(0xFF2A1930),
    tertiaryContainer = Color(0xFF553147),
    onTertiaryContainer = Color(0xFFE8B5E8),

    background = Color(0xFF0D1117),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF161B22),
    onSurface = Color(0xFFE6EDF3),

    surfaceVariant = Color(0xFF21262D),
    onSurfaceVariant = Color(0xFFB1BAC4),
    surfaceContainer = Color(0xFF1C2128),
    surfaceContainerHigh = Color(0xFF2D333B),
    surfaceContainerHighest = Color(0xFF373E47),

    outline = Color(0xFF484F58),
    outlineVariant = Color(0xFF373E47),

    error = Color(0xFFFF5449),
    onError = Color(0xFF000000),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    inversePrimary = Color(0xFF4CAF50),
    inverseSurface = Color(0xFFE6EDF3),
    inverseOnSurface = Color(0xFF0D1117),
    scrim = Color(0xFF000000)
)

@Composable
fun DiaryAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WorkoutDarkColorScheme,
        typography = Typography,
        content = content
    )
}