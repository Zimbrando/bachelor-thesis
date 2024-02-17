tasks.register<Exec>("testJpackageOutput") {
    group = "Verification"
    description = "Verifies the jpackage output correctness for the OS running the script"
    isIgnoreExitValue = true
    workingDir = rootProject.file("build/package/")
    doFirst {
        val version = rootProject.version.toString().substringBefore('-')
        // Extract the packet
        when {
            isWindows -> commandLine("msiexec", "-i", "${rootProject.name}-$version.msi", "-quiet", "INSTALLDIR=${workingDir.path}\\install")
            isMac -> commandLine("sudo", "installer", "-pkg", "${rootProject.name}-$version.pkg", "-target", "/")
            else -> {
                workingDir.resolve("install").mkdirs()
                commandLine("bsdtar", "-xf", "${rootProject.name}-$version-1.x86_64.rpm", "-C", "install")
            }
        }
    }
    doLast {
        // Check if package contains every file needed
        var execFiles: List<String>
        var appFiles: List<String>
        when {
            isWindows -> {
                execFiles = workingDir.resolve("install").listFiles().map { it.name }
                appFiles = workingDir.resolve("install/app").listFiles().map { it.name }
            }
            isMac -> {
                val root = File("/Applications/${rootProject.name}.app")
                execFiles = root.resolve("Contents/MacOS").listFiles().map { it.name }
                appFiles = root.resolve("Contents/app").listFiles().map { it.name }
            }
            else -> {
                execFiles = workingDir.resolve("install/opt/alchemist/bin").listFiles().map { it.name }
                appFiles = workingDir.resolve("install/opt/alchemist/lib/app").listFiles().map { it.name }
            }
        }
        require(rootProject.name in execFiles || "${rootProject.name}.exe" in execFiles)
        require(jpackageFull.get().mainJar in appFiles)
    }
    mustRunAfter(jpackageFull)
    finalizedBy(deleteJpackageOutput)
}