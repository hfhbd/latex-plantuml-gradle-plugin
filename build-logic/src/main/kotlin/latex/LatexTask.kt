package latex

import org.gradle.api.file.*
import org.gradle.api.logging.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.process.*
import java.io.*
import javax.inject.*
import kotlin.coroutines.*

@CacheableTask
abstract class LatexTask : SourceTask() {
    init {
        group = "latex"
    }

    @InputFiles
    @SkipWhenEmpty
    @IgnoreEmptyDirectories
    @PathSensitive(PathSensitivity.RELATIVE)
    override fun getSource(): FileTree {
        return super.getSource()
    }

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val mainTex: RegularFileProperty

    init {
        mainTex.convention(source.elements.map {
            it.singleOrNull {
                it.asFile.name == "main.tex"
            }?.let {
                RegularFile { it.asFile }
            }.ignoreKT36770()
        })
    }

    // https://github.com/gradle/gradle/issues/12388
    @Suppress("UNCHECKED_CAST")
    private inline fun <T> T?.ignoreKT36770() = this as T

    @get:Optional
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val bibFile: RegularFileProperty

    init {
        bibFile.convention(source.elements.map {
            it.singleOrNull {
                it.asFile.name == "bib.bib"
            }?.let {
                RegularFile { it.asFile }
            }.ignoreKT36770()
        })
    }

    @get:Input
    abstract val compiler: Property<String>

    init {
        compiler.convention("lualatex")
    }

    @get:OutputDirectory
    abstract val outputFolder: DirectoryProperty

    @get:Inject
    abstract val execOperations: ExecOperations

    @TaskAction
    fun action() {
        val main = mainTex.asFile.get()
        val source = main.parentFile.absolutePath
        val outputFolder = outputFolder.get().asFile
        val compiler = compiler.get()
        val bibFile = bibFile.asFile.orNull?.nameWithoutExtension

        fun runLatex() = exec(
            listOf(
            compiler,
            "-file-line-error",
            "-interaction=nonstopmode",
            "-synctex=1",
            "-output-format=pdf",
            "-output-directory=${outputFolder.absolutePath}",
            main.absolutePath),
            workingDirectory = main.parentFile
        )

        runLatex()
        if (bibFile != null) {
            exec(listOf("biber", "--input-directory=$source", bibFile), workingDirectory = outputFolder)

            repeat(2) {
                runLatex()
            }
        }
        println(File(outputFolder, "main.pdf").absolutePath)
    }

    private fun exec(cmd: List<String>, workingDirectory: File) {
        execOperations.exec {
            commandLine(cmd)
            workingDir = workingDirectory
            logging.captureStandardOutput(LogLevel.INFO)
        }.assertNormalExitValue()
    }
}
