package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.db.AppDatabase
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.CompletedWorkout
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.Note
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutPreferences
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutSuggestion
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository.CalendarRepository
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository.RoomCalendarRepository
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository.RoomWorkoutRepository
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import java.util.UUID

data class CalendarUiState(
    val notes: List<Note> = emptyList(),
    val workoutSuggestions: List<WorkoutSuggestion> = emptyList(),
    val selectedDate: LocalDate? = null,
    val currentWeekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
) {
    fun getWorkoutForDate(date: LocalDate): WorkoutSuggestion? {
        return workoutSuggestions.find { it.date == date }
    }

    fun getFilteredNotes(): List<Note> {
        return if (searchQuery.isBlank()) {
            notes
        } else {
            notes.filter { note ->
                note.title.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    fun getFilteredNotesForDate(date: LocalDate): List<Note> {
        return getFilteredNotes().filter { it.date == date }
    }
}

class CalendarViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val notesRepository: CalendarRepository = RoomCalendarRepository(
        AppDatabase.getDatabase(application).noteDao()
    )

    private val workoutRepository: WorkoutRepository = RoomWorkoutRepository(
        AppDatabase.getDatabase(application).workoutDao()
    )

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadData()
        viewModelScope.launch {
            val prefs = workoutRepository.getWorkoutPreferences()
            if (prefs.selectedDays.isEmpty() || prefs.workoutTypes.isEmpty()) {
                workoutRepository.saveWorkoutPreferences(
                    WorkoutPreferences(
                        selectedDays = "",
                        workoutTypes = "",
                        lastWorkoutRotation = 0
                    )
                )
            }
        }
    }

    private fun loadData() {
        loadNotes()
        loadWorkouts()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val notes = notesRepository.getNotes()
                _uiState.value = _uiState.value.copy(
                    notes = notes,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            try {
                val workoutSuggestions = workoutRepository.getWorkoutSuggestionsForWeek(_uiState.value.currentWeekStart)
                _uiState.value = _uiState.value.copy(workoutSuggestions = workoutSuggestions)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load workouts"
                )
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun navigateToWeek(weekStart: LocalDate) {
        _uiState.value = _uiState.value.copy(currentWeekStart = weekStart)
        loadWorkouts()
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            try {
                val noteToAdd = if (note.id.isEmpty()) {
                    note.copy(id = UUID.randomUUID().toString())
                } else {
                    note
                }
                notesRepository.addNote(noteToAdd)
                loadNotes()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to add note")
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                notesRepository.updateNote(note)
                loadNotes()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to update note")
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            try {
                notesRepository.deleteNote(noteId)
                loadNotes()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to delete note")
            }
        }
    }

    fun completeWorkout(workout: CompletedWorkout) {
        viewModelScope.launch {
            try {
                workoutRepository.addCompletedWorkout(workout)
                loadWorkouts()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to complete workout")
            }
        }
    }

    fun deleteCompletedWorkout(workoutId: String) {
        viewModelScope.launch {
            try {
                workoutRepository.deleteCompletedWorkout(workoutId)
                loadWorkouts()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to delete workout")
            }
        }
    }

    fun updateWorkoutPreferences(preferences: WorkoutPreferences) {
        viewModelScope.launch {
            try {
                workoutRepository.saveWorkoutPreferences(preferences)
                loadWorkouts()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to update workout preferences")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleSearch() {
        val newSearchActive = !_uiState.value.isSearchActive
        _uiState.value = _uiState.value.copy(
            isSearchActive = newSearchActive,
            searchQuery = if (newSearchActive) _uiState.value.searchQuery else ""
        )
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            isSearchActive = false
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}