open class CustomJPackageTask() : JPackageTask() {
    @TaskAction
    override fun action() {
        var types: List<ImageType>
        when {
            isWindows -> types = listOf(ImageType.EXE, ImageType.MSI)
            isMac -> types = listOf(ImageType.DMG, ImageType.PKG)
            else -> types = listOf(ImageType.DEB, ImageType.RPM)
        }
        types.forEach {
            setType(it)
            super.action()
        }
    }
}