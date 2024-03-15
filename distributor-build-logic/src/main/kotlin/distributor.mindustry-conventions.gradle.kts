import fr.xpdustry.toxopid.dsl.mindustryDependencies
import fr.xpdustry.toxopid.spec.ModMetadata

plugins {
    id("net.kyori.indra")
    id("com.github.johnrengelman.shadow")
    id("fr.xpdustry.toxopid")
}

val extension = project.extensions.findOrCreateExtension<DistributorModuleExtension>(DistributorModuleExtension.EXTENSION_NAME)

toxopid {
    compileVersion.set(libs.versions.mindustry)
    platforms.add(fr.xpdustry.toxopid.spec.ModPlatform.HEADLESS)
}

dependencies {
    mindustryDependencies()
}

afterEvaluate {
    dependencies {
        for (dependency in extension.dependencies.get()) {
            compileOnly(dependency)
        }
    }
}

tasks.runMindustryClient {
    mods.setFrom()
}

tasks.runMindustryServer {
    mods.from(tasks.shadowJar)
}

// Cursed way to collect dependent distributor modules
gradle.projectsEvaluated {
    tasks.runMindustryServer {
        mods.from(collectAllPluginDependencies())
    }
}

tasks.shadowJar {
    doFirst {
        val metadata =
            ModMetadata(
                name = extension.identifier.get(),
                displayName = extension.display.get(),
                description = extension.description.get(),
                version = project.version.toString(),
                repo = "xpdustry/distributor",
                author = "xpdustry",
                minGameVersion = libs.versions.mindustry.get().substring(1),
                main = extension.main.get(),
                java = true,
                hidden = true,
                dependencies =
                    extension.dependencies.get()
                        .map { it.dependencyProject.extensions.getByType<DistributorModuleExtension>().identifier.get() }
                        .toMutableList(),
            )

        val temp = temporaryDir.resolve("plugin.json")
        temp.writeText(metadata.toJson(true))
        from(temp)
    }

    archiveClassifier.set("plugin")
    from(rootProject.file("LICENSE.md")) {
        into("META-INF")
    }
    mergeServiceFiles()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
