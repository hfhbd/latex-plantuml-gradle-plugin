package plantuml

import org.gradle.api.*
import org.gradle.api.plugins.*

class PlantumlPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(BasePlugin::class.java)
        project.tasks.register("savePNG", PlantumlTask::class.java)
    }
}
