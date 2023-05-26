package bakiev.artour.asyncu.services

interface GoogleDrive {
    /**
     * @param parentFolderId - id for the folder to query list of child folders or `null` if the
     *                         content for the `root` folder is queried
     */
    fun childFolders(parentFolderId: String): List<File>

    interface File {
        val id: String
        val name: String
        fun changeCurrentPath(currentPath: MutableList<String>)

        private class Up(override val id: String) : File {
            override val name: String = ".."
            override fun changeCurrentPath(currentPath: MutableList<String>) {
                currentPath.removeLast()
            }
        }

        private class Down(override val id: String, override val name: String) : File {
            override fun changeCurrentPath(currentPath: MutableList<String>) {
                currentPath.add(id)
            }
        }

        companion object {
            fun folder(id: String, name: String): File = Down(id, name)
            fun parentFolder(id: String): File = Up(id)
        }
    }

    companion object {
        const val rootFolderId: String = "root"
    }
}
