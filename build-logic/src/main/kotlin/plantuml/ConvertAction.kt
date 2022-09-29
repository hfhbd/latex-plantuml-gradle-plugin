package plantuml

import net.sourceforge.plantuml.*
import org.gradle.workers.*
import java.io.*

abstract class ConvertAction: WorkAction<PlantUmlConvertParameter> {
    override fun execute() {
        val plantUmlFile= parameters.pantumlFile.asFile.get()
        val outputFile = parameters.pngFile.asFile.get()
        plantUmlFile.useLines {
            requireNotNull(it.singleOrNull { it == "@startuml" }) {
                "${plantUmlFile.absolutePath} contains 0 or more than 1 diagrams"
            }
        }
        val error = ErrorStatus.init()
        outputFile.outputStream().use { png ->
            PrintStream(png, true).use { png ->
                plantUmlFile.inputStream().use {
                    Pipe(Option(), png, it, Charsets.UTF_8.toString()).managePipe(error)

                    require(!error.hasError()) {
                        "${plantUmlFile.absolutePath} returned with ${error.exitCode}"
                    }
                }
            }
        }
    }
}
