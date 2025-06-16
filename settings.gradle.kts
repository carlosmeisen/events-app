pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "PortugueseEvents"
include(":app")

// Core Modules
include(":core")
include(":core:core-ui")
include(":core:core-navigation")
include(":core:core-testing")
include(":core:core-common")

// Data Modules
include(":data")
include(":data:data-api")
include(":data:data-local")
include(":data:data-repository")

// Domain Modules
include(":domain")
include(":domain:domain-model")
include(":domain:domain-repository")
include(":domain:domain-usecase")

// Feature Modules
include(":feature")
include(":feature:feature-home")
include(":feature:feature-calendar")
include(":feature:feature-settings")
include(":feature:feature-login")
include(":feature:feature-favorites")

//Navigation Modules
include(":navigation")

//Mappers Modules
include(":mapper")

//Infrastructures Modules
include(":infrastructure")
include(":navigation:navigation-ui")
include(":navigation:navigation-engine")
include(":analytics")
include(":analytics:tracking")
include(":analytics:logging")
include(":domain:domain-analytics")
include(":domain:domain-logger")
include(":core:core-logging")
