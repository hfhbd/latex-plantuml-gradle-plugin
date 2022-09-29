package plantuml

import net.sourceforge.plantuml.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.gradle.work.NormalizeLineEndings
import org.gradle.workers.*
import java.io.*
import javax.inject.*

@CacheableTask
abstract class PlantumlTask : DefaultTask() {
    init {
        group = "plantuml"
    }

    @get:NormalizeLineEndings
    @get:Incremental
    @get:InputFiles
    @get:IgnoreEmptyDirectories
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val source: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputFolder: DirectoryProperty

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun action(inputChanges: InputChanges) {
        val queue = workerExecutor.noIsolation()
        for (change in inputChanges.getFileChanges(source)) {
            if (change.fileType == FileType.DIRECTORY) continue

            val targetFile = outputFolder.file(change.normalizedPath).get().asFile
            if (change.changeType == ChangeType.REMOVED) {
                targetFile.delete()
            } else {
                queue.submit(ConvertAction::class.java) {
                    pantumlFile.set(change.file)
                    pngFile.set(File(targetFile.parent, "${targetFile.nameWithoutExtension}.png"))
                }
            }
        }
    }
}
