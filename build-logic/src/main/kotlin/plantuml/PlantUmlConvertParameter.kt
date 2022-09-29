package plantuml

import org.gradle.api.file.*
import org.gradle.workers.*

interface PlantUmlConvertParameter: WorkParameters {
    val pantumlFile: RegularFileProperty
    val pngFile: RegularFileProperty
}
