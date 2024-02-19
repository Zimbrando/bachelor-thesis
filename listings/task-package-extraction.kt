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

