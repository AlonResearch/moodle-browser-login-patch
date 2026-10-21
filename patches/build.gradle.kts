group = "app.alonresearch"

patches {
    about {
        name = "Moodle Browser Login Patch"
        description = "Use the system browser for Moodle site login on Moodle Android 5.2.1."
        source = "https://github.com/AlonResearch/moodle-browser-login-patch"
        author = "AlonResearch"
        contact = "https://github.com/AlonResearch/moodle-browser-login-patch/issues"
        website = "https://github.com/AlonResearch/moodle-browser-login-patch"
        license = "GPLv3"
    }
}

val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }
    publish {
        dependsOn("generatePatchesList")
    }
}
