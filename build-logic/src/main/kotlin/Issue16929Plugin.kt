import org.gradle.api.*
import org.gradle.api.initialization.*

// https://github.com/gradle/gradle/issues/16929
class Issue16929Plugin: Plugin<Settings> {
    override fun apply(target: Settings) {

    }
}
