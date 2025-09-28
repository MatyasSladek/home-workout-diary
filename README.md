# Home Workout App

A comprehensive Android fitness application designed to help users track their workouts, manage exercise schedules, and stay motivated with personalized fitness plans.

## Features

### 📅 Calendar & Workout Management
- **Weekly View**: Visual calendar displaying workout schedule and notes
- **Workout Tracking**: Mark workouts as completed and track progress
- **Note System**: Add daily notes related to your fitness journey
- **Search Functionality**: Quickly find notes by title
- **Workout Suggestions**: AI-powered workout recommendations based on user preferences

### 👤 User Profile Management
- **Personalized Profiles**: Store name, surname, and fitness preferences
- **Exercise Schedule**: Select preferred workout days
- **Workout Type Selection**: Choose from various workout categories (Legs, Belly, Back, Arms)
- **Profile Editing**: Easy-to-use interface for updating preferences

### 🌤️ Weather Integration
- **Weather Dashboard**: Check current weather conditions
- **Location-based Forecasts**: Get weather information for any city
- **Exercise Planning**: Plan workouts based on weather conditions

### 🔔 Smart Notifications & Reminders
- **Workout Reminders**: Get notified about scheduled workouts
- **Customizable Timing**: Set preferred reminder times
- **Notification Actions**: Mark complete or snooze directly from notifications
- **Permission Management**: Comprehensive permission handling for Android 13+

### 💾 Data Persistence
- **Room Database**: Local storage for notes and workout data
- **DataStore Preferences**: Secure storage for user preferences
- **Offline Functionality**: Full app functionality without internet connection

## Technical Architecture

### Built With
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI framework
- **MVVM Architecture**: Clean separation of concerns
- **Room Database**: Local data persistence
- **DataStore**: Preferences storage
- **ThreeTenABP**: Java 8+ time API backport for Android

### Key Components

#### ViewModels
- `CalendarViewModel`: Manages calendar data and workout scheduling
- `UserProfileEditorViewModel`: Handles user profile editing
- `WeatherViewModel`: Manages weather data fetching and display

#### Repositories
- `CalendarRepository`: Note management operations
- `WorkoutRepository`: Workout scheduling and tracking
- `WeatherRepository`: Weather API integration
- `UserProfileRepository`: User preference management

#### Utility Classes
- `NotificationHelper`: Handles workout reminder notifications
- `WorkoutAlarmManager`: Manages exact alarm scheduling
- `PermissionManager`: Handles runtime permissions

## Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 21 (API Level 21) or higher
- Kotlin 1.5.30 or later

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Build and run the application

### Required Permissions
The app requires the following permissions:
- **POST_NOTIFICATIONS** (Android 13+): For workout reminders
- **SCHEDULE_EXACT_ALARM**: For precise workout scheduling

## Usage

### Getting Started
1. **Initial Setup**: Complete the user profile setup with your preferences
2. **Schedule Workouts**: Select your exercise days and preferred workout types
3. **Set Reminders**: Grant permissions for workout notifications
4. **Start Tracking**: Use the calendar to track your workout progress

### Main Screens

#### Calendar Screen
- View your weekly workout schedule
- Add daily notes and track completed workouts
- Search through your fitness notes
- Manage workout completions

#### User Profile Screen
- View and edit personal information
- Modify exercise schedule and preferences
- Update workout type selections

#### Weather Dashboard
- Check current weather conditions
- Plan workouts based on weather
- Search weather for different locations

### Workout Types
The app supports four main workout categories:
- **Legs**: Squats, Lunges, Calf Raises
- **Belly**: Crunches, Planks, Leg Raises
- **Back**: Pull-ups, Rows, Back Extensions
- **Arms**: Push-ups, Bicep Curls, Tricep Dips

## Database Schema

### Entities
- **NoteEntity**: Stores user notes with date and content
- **WorkoutPreferences**: User's exercise preferences
- **CompletedWorkout**: Tracks completed workout sessions

### Data Flow
1. User preferences stored in DataStore
2. Workout data persisted in Room database
3. Real-time updates through Flow observables
4. UI automatically updates with state changes

## Customization

### Theming
The app uses Material Design 3 theming with customizable color schemes. Modify the `DiaryAppTheme` in the theme package to change the app's appearance.

### Workout Customization
Users can customize:
- Exercise days of the week
- Preferred workout types
- Notification timing
- Weather location preferences

## Contributing

### Code Style
- Follow Kotlin coding conventions
- Use descriptive variable names
- Implement proper error handling
- Write comprehensive comments for complex logic

### Architecture Guidelines
- Follow MVVM pattern
- Use StateFlow for UI state management
- Implement proper dependency injection
- Write unit tests for ViewModels

## Support

For issues and feature requests, please contact the development team or create an issue in the project repository.

## License

This project is developed for educational purposes as part of the ZAN course at Czech Technical University.

---

**Developed by**:
- Matyáš Sládek
- CTU in Prague, Faculty of Electrical Engineering

*Last updated: 28 September 2025*