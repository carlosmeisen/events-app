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
include(":domain:domain-models")
include(":domain:domain-repository")
include(":domain:domain-usecase")

// Feature Modules
include(":features")
include(":features:feature-home")
include(":features:feature-calendar")
include(":features:feature-settings")
include(":features:feature-login")
include(":features:feature-favorites")

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
include(":domain:domain-services")
include(":services")
