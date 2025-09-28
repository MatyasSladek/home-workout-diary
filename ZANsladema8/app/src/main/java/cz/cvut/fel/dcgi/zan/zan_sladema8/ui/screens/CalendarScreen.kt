package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.CompletedWorkout
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.Note
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutSuggestion
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.MainBottomNavigation
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.BottomNavigationItem
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels.CalendarViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    mainBottomNavigationItems: List<BottomNavigationItem>,
    currentRoute: String?
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showNoteDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showWorkoutDialog by remember { mutableStateOf(false) }
    var selectedWorkout by remember { mutableStateOf<WorkoutSuggestion?>(null) }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            if (uiState.isSearchActive) {
                SearchTopAppBar(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCloseSearch = viewModel::clearSearch
                )
            } else {
                TopAppBar(
                    title = { Text(text = "Calendar") },
                    actions = {
                        IconButton(onClick = viewModel::toggleSearch) {
                            Icon(Icons.Default.Search, contentDescription = "Search notes")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        },
        bottomBar = {
            MainBottomNavigation(mainBottomNavigationItems, currentRoute)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.selectedDate == null) {
                        viewModel.selectDate(LocalDate.now())
                    }
                    showNoteDialog = true
                    editingNote = null
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.isSearchActive && uiState.searchQuery.isNotBlank()) {
                SearchResultsSection(
                    filteredNotes = uiState.getFilteredNotes(),
                    onNoteClick = { note ->
                        editingNote = note
                        viewModel.selectDate(note.date)
                        showNoteDialog = true
                    },
                    onEditNote = { note ->
                        editingNote = note
                        showNoteDialog = true
                    },
                    onDeleteNote = { note ->
                        viewModel.deleteNote(note.id)
                    }
                )
            } else {
                WeekHeader(
                    weekStart = uiState.currentWeekStart,
                    onPreviousWeek = {
                        viewModel.navigateToWeek(uiState.currentWeekStart.minusWeeks(1))
                    },
                    onNextWeek = {
                        viewModel.navigateToWeek(uiState.currentWeekStart.plusWeeks(1))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                WeekView(
                    weekStart = uiState.currentWeekStart,
                    notes = uiState.getFilteredNotes(),
                    workoutSuggestions = uiState.workoutSuggestions,
                    selectedDate = uiState.selectedDate,
                    onDateClick = { date ->
                        viewModel.selectDate(date)
                    },
                    onNoteClick = { note ->
                        editingNote = note
                        viewModel.selectDate(note.date)
                        showNoteDialog = true
                    },
                    onWorkoutClick = { workout ->
                        selectedWorkout = workout
                        viewModel.selectDate(workout.date)
                        showWorkoutDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                uiState.selectedDate?.let { date ->
                    DayDetailsSection(
                        date = date,
                        notes = uiState.getFilteredNotesForDate(date),
                        workout = uiState.getWorkoutForDate(date),
                        onEditNote = { note ->
                            editingNote = note
                            showNoteDialog = true
                        },
                        onDeleteNote = { note ->
                            viewModel.deleteNote(note.id)
                        },
                        onCompleteWorkout = { workout ->
                            val completedWorkout = CompletedWorkout(
                                id = "",
                                date = workout.date,
                                workoutType = workout.workoutType,
                                exercises = workout.workoutType.exercises.joinToString(","),
                                duration = 30,
                                notes = ""
                            )
                            viewModel.completeWorkout(completedWorkout)
                        },
                        onDeleteWorkout = { workout ->
                            workout.completedWorkout?.let {
                                viewModel.deleteCompletedWorkout(it.id)
                            }
                        }
                    )
                }
            }
        }

        if (showNoteDialog) {
            val dateForDialog = uiState.selectedDate ?: LocalDate.now()
            NoteDialog(
                date = dateForDialog,
                existingNote = editingNote,
                onDismiss = {
                    showNoteDialog = false
                    editingNote = null
                },
                onSave = { note ->
                    if (editingNote != null) {
                        viewModel.updateNote(note)
                    } else {
                        viewModel.addNote(note)
                    }
                    showNoteDialog = false
                    editingNote = null
                }
            )
        }

        if (showWorkoutDialog && selectedWorkout != null) {
            WorkoutDialog(
                workout = selectedWorkout!!,
                onDismiss = {
                    showWorkoutDialog = false
                    selectedWorkout = null
                },
                onComplete = { workout ->
                    val completedWorkout = CompletedWorkout(
                        id = "",
                        date = workout.date,
                        workoutType = workout.workoutType,
                        exercises = workout.workoutType.exercises.joinToString(","),
                        duration = 30,
                        notes = ""
                    )
                    viewModel.completeWorkout(completedWorkout)
                    showWorkoutDialog = false
                    selectedWorkout = null
                }
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search notes by title...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                }
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseSearch) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close search")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun SearchResultsSection(
    filteredNotes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Search Results (${filteredNotes.size} notes found)",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredNotes.isEmpty()) {
                Text(
                    text = "No notes found matching your search.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(filteredNotes) { note ->
                        SearchResultNoteItem(
                            note = note,
                            onNoteClick = { onNoteClick(note) },
                            onEditNote = { onEditNote(note) },
                            onDeleteNote = { onDeleteNote(note) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultNoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onEditNote: () -> Unit,
    onDeleteNote: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onNoteClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = note.date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (note.content.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row {
                IconButton(onClick = onEditNote) {
                    Icon(Icons.Default.Edit, "Edit note")
                }
                IconButton(onClick = onDeleteNote) {
                    Icon(Icons.Default.Delete, "Delete note")
                }
            }
        }
    }
}

@Composable
fun WeekHeader(
    weekStart: LocalDate,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    val weekEnd = weekStart.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("MMM d")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous week")
        }

        Text(
            text = "${weekStart.format(formatter)} - ${weekEnd.format(formatter)}, ${weekStart.year}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onNextWeek) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next week")
        }
    }
}

@Composable
    fun WeekView(
        weekStart: LocalDate,
        notes: List<Note>,
        workoutSuggestions: List<WorkoutSuggestion>,
        selectedDate: LocalDate?,
        onDateClick: (LocalDate) -> Unit,
        onNoteClick: (Note) -> Unit,
        onWorkoutClick: (WorkoutSuggestion) -> Unit
    ) {
        val days = (0..6).map { weekStart.plusDays(it.toLong()) }
        val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    days.forEachIndexed { index, date ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayNames[index],
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal,
                                color = if (date == LocalDate.now()) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    days.forEach { date ->
                        val dayNotes = notes.filter { it.date == date }
                        val dayWorkout = workoutSuggestions.find { it.date == date }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (date == selectedDate) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                                .clickable { onDateClick(date) }
                                .padding(4.dp)
                        ) {
                            dayWorkout?.let { workout ->
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                ) {
                                    WorkoutIndicator(
                                        workout = workout,
                                        onClick = { onWorkoutClick(workout) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            LazyColumn {
                                items(dayNotes.take(2)) { note ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 1.dp)
                                            .clickable { onNoteClick(note) },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                    ) {
                                        Text(
                                            text = note.title,
                                            modifier = Modifier.padding(4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                if (dayNotes.size > 2) {
                                    item {
                                        Text(
                                            text = "+${dayNotes.size - 2} more",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WorkoutIndicator(
        workout: WorkoutSuggestion,
        onClick: () -> Unit
    ) {
        val workoutColor = when (workout.workoutType) {
            WorkoutType.LEGS -> Color(0xFF4CAF50)
            WorkoutType.BELLY -> Color(0xFF2196F3)
            WorkoutType.BACK -> Color(0xFFFF9800)
            WorkoutType.ARMS -> Color(0xFF9C27B0)
        }

        val workoutLetter = workout.workoutType.name.first().toString()

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (workout.isCompleted) workoutColor
                    else workoutColor.copy(alpha = 0.3f)
                )
                .border(
                    width = if (workout.isCompleted) 0.dp else 2.dp,
                    color = workoutColor,
                    shape = CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (workout.isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Completed workout",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = workoutLetter,
                    color = workoutColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    fun DayDetailsSection(
        date: LocalDate,
        notes: List<Note>,
        workout: WorkoutSuggestion?,
        onEditNote: (Note) -> Unit,
        onDeleteNote: (Note) -> Unit,
        onCompleteWorkout: (WorkoutSuggestion) -> Unit,
        onDeleteWorkout: (WorkoutSuggestion) -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Details for ${date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                workout?.let { w ->
                    Text(
                        text = "Workout",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = w.workoutType.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = w.workoutType.exercises.joinToString(", "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row {
                                if (!w.isCompleted) {
                                    IconButton(onClick = { onCompleteWorkout(w) }) {
                                        Icon(Icons.Default.Check, "Complete workout")
                                    }
                                }
                                IconButton(onClick = { onDeleteWorkout(w) }) {
                                    Icon(Icons.Default.Delete, "Delete workout")
                                }
                            }
                        }
                    }
                }

                if (workout != null && notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (notes.isNotEmpty()) {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn {
                        items(notes) { note ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = note.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (note.content.isNotEmpty()) {
                                            Text(
                                                text = note.content,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Row {
                                        IconButton(onClick = { onEditNote(note) }) {
                                            Icon(Icons.Default.Edit, "Edit note")
                                        }
                                        IconButton(onClick = { onDeleteNote(note) }) {
                                            Icon(Icons.Default.Delete, "Delete note")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NoteDialog(
        date: LocalDate,
        existingNote: Note?,
        onDismiss: () -> Unit,
        onSave: (Note) -> Unit
    ) {
        var title by remember { mutableStateOf(existingNote?.title ?: "") }
        var content by remember { mutableStateOf(existingNote?.content ?: "") }

        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = if (existingNote != null) "Edit Note" else "Add Note",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val note = existingNote?.copy(
                                    title = title,
                                    content = content
                                ) ?: Note(
                                    id = "",
                                    title = title,
                                    content = content,
                                    date = date
                                )
                                onSave(note)
                            },
                            enabled = title.isNotBlank()
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WorkoutDialog(
        workout: WorkoutSuggestion,
        onDismiss: () -> Unit,
        onComplete: (WorkoutSuggestion) -> Unit
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "${workout.workoutType.name} Workout",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Exercises:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    workout.workoutType.exercises.forEach { exercise ->
                        Text(
                            text = "• $exercise",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (!workout.isCompleted) {
                            Button(onClick = { onComplete(workout) }) {
                                Text("Complete")
                            }
                        }
                    }
                }
            }
        }
    }