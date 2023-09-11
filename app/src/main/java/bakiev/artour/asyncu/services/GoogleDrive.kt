package bakiev.artour.asyncu.services

interface GoogleDrive {
    /**
     * @param parentFolderId - id for the folder to query list of child folders or `null` if the
     *                         content for the `root` folder is queried
     */
//    fun childFolders(parentFolderId: String?): List<File>
    fun childFolders(parentFolder: File): List<File>

    interface File {
        val id: String
        val name: String
        val displayName: String
        val parent: File?
//        fun changeCurrentPath(currentPath: MutableList<File>)

        private class Up(file: File) : File {
            override val id: String = file.id
            override val name: String = file.name
            override val displayName = ".."
            override val parent: File? = file.parent
//            override fun changeCurrentPath(currentPath: MutableList<File>) {
//                currentPath.removeLast()
//            }
        }

        private class Down(override val id: String, override val name: String, override val parent: File?) : File {
            //            override val path: List<String> = parentFolder.path + parentFolder.name
//            override fun changeCurrentPath(currentPath: MutableList<File>) {
//                currentPath.add(this)
//            }
            override val displayName: String = name
        }

        companion object {
            fun folder(id: String, name: String, parentFolder: File): File = Down(id, name, parentFolder)
            fun parentFolder(parentFolder: File): File = Up(parentFolder)

            val root: File = object : File {
                override val id: String = "root"
                override val name: String = "My Drive"
                override val displayName: String = name
                override val parent: File? = null
            }
        }
    }

//    companion object {
//        const val rootFolderId: String = "root"
//    }
}

fun GoogleDrive.File.path(): String = buildString {
    var currentFile: GoogleDrive.File? = this@path
//    var parent = currentFile.parent
    while (currentFile != null) {
        if (isNotEmpty()) {
            insert(0, '/')
        }
        insert(0, currentFile.name)
        currentFile = currentFile.parent
//        append(currentFile.name)
//        append("/")
//        currentFile = parent
//        parent = currentFile?.parent
    }
//    append(currentFile.name)
}
