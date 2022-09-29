package latex

import org.gradle.api.*
import org.gradle.api.plugins.*

class LatexPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(BasePlugin::class.java)
        project.tasks.register("runLatex", LatexTask::class.java)
    }
}
