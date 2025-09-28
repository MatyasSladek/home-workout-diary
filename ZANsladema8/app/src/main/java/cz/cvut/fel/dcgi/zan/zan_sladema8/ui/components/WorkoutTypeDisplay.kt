package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType

@Composable
fun WorkoutTypeDisplay(
    workoutTypes: Set<WorkoutType>,
    modifier: Modifier = Modifier
) {
    val workoutColors = mapOf(
        WorkoutType.LEGS to Color(0xFF4CAF50),
        WorkoutType.BELLY to Color(0xFF2196F3),
        WorkoutType.BACK to Color(0xFFFF9800),
        WorkoutType.ARMS to Color(0xFF9C27B0)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        workoutTypes.forEach { workout ->
            val color = workoutColors[workout] ?: Color.Gray

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = workout.name.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = workout.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun WorkoutTypeSelector(
    selectedWorkoutTypes: Set<WorkoutType>,
    onWorkoutTypesChanged: (Set<WorkoutType>) -> Unit,
    modifier: Modifier = Modifier
) {
    val workoutColors = mapOf(
        WorkoutType.LEGS to Color(0xFF4CAF50),
        WorkoutType.BELLY to Color(0xFF2196F3),
        WorkoutType.BACK to Color(0xFFFF9800),
        WorkoutType.ARMS to Color(0xFF9C27B0)
    )

    Column(modifier = modifier) {
        WorkoutType.entries.forEach { workoutType ->
            val isSelected = workoutType in selectedWorkoutTypes
            val backgroundColor = if (isSelected) {
                workoutColors[workoutType]?.copy(alpha = 0.2f) ?: MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .clickable {
                        val newTypes = if (isSelected) {
                            selectedWorkoutTypes - workoutType
                        } else {
                            selectedWorkoutTypes + workoutType
                        }
                        onWorkoutTypesChanged(newTypes)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isSelected) 8.dp else 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                workoutColors[workoutType] ?: Color.Gray,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = workoutType.name.first().toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = workoutType.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = workoutType.exercises.take(3).joinToString(", ") +
                                    if (workoutType.exercises.size > 3) "..." else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (isSelected) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "Selected",
                            tint = workoutColors[workoutType] ?: MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}