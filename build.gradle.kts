plugins {
    latex
    plantuml
}

tasks {
    savePNG {
        source.setFrom("src/latex/resources/uml")
        outputFolder.set(layout.buildDirectory.dir("latex/uml"))
    }

    runLatex {
        dependsOn(savePNG)
        source("src/latex/main")
        outputFolder.set(layout.buildDirectory.dir("latex/sample"))
        bibFile.set(file("src/latex/main/main.bib"))
    }
}
