# PortugueseEvents (Working Title)

PortugueseEvents is an Android application designed to help users discover and manage information about various events. The app is built with a focus on a modern, reactive architecture using Kotlin and the latest Android Jetpack libraries.

## 🌟 Features

*   **Event Discovery:** (Details to be added - e.g., Browse, search, view event details)
*   **Calendar View:** Likely for viewing events by date.
*   **Favorites:** Allows users to save and quickly access preferred events.
*   **Settings:**
    *   **Theme Customization:** Switch between Light and Dark themes.
    *   Option to follow the system's current theme setting.
*   **Intuitive Navigation:** Smooth and predictable navigation between different sections of the app.

## 🛠️ Technical Stack & Architecture

This project leverages a modern Android development stack and emphasizes a clean, scalable architecture:

*   **Programming Language:** [Kotlin](https://kotlinlang.org/) (100%)
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for building a declarative and reactive UI.
*   **Architecture:**
    *   Follows **Clean Architecture** principles, separating concerns into distinct layers (Domain, Data, Presentation).
    *   **MVVM (Model-View-ViewModel)** for the presentation layer.
*   **Dependency Injection:** [Koin](https://insert-koin.io/) for managing dependencies and promoting decoupling.
*   **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html) for managing background tasks, asynchronous operations, and reactive data streams.
*   **Navigation:**
    *   Built upon [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
    *   Custom command-driven navigation system featuring a central `NavigationOrchestrator`, `AppNavigator`, and `NavigationValidator`s for robust and testable screen transitions.
*   **Modularization:**
    *   Multi-module Gradle project (e.g., `:app`, `:core`, `:data:data-local`, `:domain:domain-usecase`, `:feature:feature-home`, `:navigation:navigation-engine`) to enhance separation of concerns, build times, and maintainability.
*   **Data Persistence:**
    *   [Jetpack DataStore (Preferences)](https://developer.android.com/topic/libraries/architecture/datastore) for storing user preferences like theme settings.
    *   (Details about other data sources like local database or remote API to be added as applicable).

## 🏗️ Project Structure (Key Modules)

The project is divided into several modules to ensure a clear separation of responsibilities:

*   `app`: The main application module, integrating all other modules.
*   `core`: Common utilities, base classes, and core functionalities shared across the app.
    *   `core-ui`: Base UI components, themes, and Compose utilities.
    *   `core-navigation`: Core interfaces and models for the navigation system.
    *   `core-logging`: Abstractions for logging.
*   `data`: Handles data sources and repositories.
    *   `data-local`: Local data persistence (e.g., DataStore, Room database).
    *   `data-api`: (If applicable) Network API definitions and data transfer objects.
    *   `data-repository`: Implementations of repository interfaces from the domain layer.
*   `domain`: Contains the core business logic, use cases, and domain models, independent of Android framework.
    *   `domain-model`: Plain Kotlin data classes representing core entities.
    *   `domain-repository`: Interfaces for data repositories.
    *   `domain-usecase`: Business logic and interaction orchestrators.
*   `feature`: Individual feature modules.
    *   `feature-home`: Home screen functionality.
    *   `feature-calendar`: Calendar screen functionality.
    *   `feature-settings`: Settings screen functionality.
    *   `feature-favorites`: Favorites screen functionality.
*   `navigation`: Modules related to the app's navigation system.
    *   `navigation-engine`: The core implementation of the navigation orchestrator and navigator.
    *   `navigation-ui`: (If applicable) UI components specific to navigation, like the bottom bar.
*   `analytics`: Modules for analytics and logging.
    *   `logging`: Implementation of logging utilities.
*   `infrastructure`: (If applicable) Platform-specific implementations like external app launchers.
*   `mapper`: (If applicable) Data mapping utilities between layers.

*(This structure is based on the `settings.gradle.kts` provided and can be refined as the project evolves.)*

## 🚀 Getting Started

**(To be added: Instructions on how to build and run the project, prerequisites, API keys if any, etc.)**

1.  Clone the repository:
2.  Open the project in Android Studio.
3.  Let Gradle sync and download dependencies.
4.  Run the `app` configuration on an emulator or physical device.

## 🤝 Contributing

**(To be added: Guidelines for contributing if this is an open-source project or for team collaboration.)**

---

*This README is a work in progress and will be updated as the project develops.*
