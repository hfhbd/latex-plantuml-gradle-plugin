import plantuml.*

plugins {
    id("base")
}

tasks.register("savePNG", PlantumlTask::class)
