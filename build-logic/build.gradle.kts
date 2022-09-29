plugins {
    `kotlin-dsl`
    base
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("net.sourceforge.plantuml:plantuml:1.2022.8")
}

gradlePlugin {
    plugins {
        create("latex") {
            id = "latex"
            implementationClass = "latex.LatexPlugin"
        }
        create("plantuml") {
            id = "plantuml"
            implementationClass = "plantuml.PlantumlPlugin"
        }
        create("pluginExtensions") {
            id = "pluginextensions"
            implementationClass = "ExtensionsPlugin"
        }
    }
}
