import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

private const val TEMP_FILE_NAME_TAIL = ".temp"
private const val TARGET_TO_REMOVE = "\"></string>"

private const val ERROR_BLANK_PATH = "error! This path is incorrect"
private const val ERROR_FILE_NOT_EXISTS = "error! This file is not exists"
private const val ERROR_OTHER = "error!"
private const val SUCCESS = "SUCCESS"


fun main(args: Array<String>) {

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    while (true) {
        println("please enter the strings file: ")
        println(getFileAndDealIt())
        println()
    }
}

private fun getFileAndDealIt(): String {
    val filePath = readLine()
    if (filePath.isNullOrBlank()) {
        return ERROR_BLANK_PATH
    }
    val file = File(filePath)
    if (!file.exists()) {
        return ERROR_FILE_NOT_EXISTS
    }
    try {
        removeEmptyString(file)
    } catch (e: IOException) {
        return "$ERROR_OTHER $e"
    }
    return SUCCESS
}

private fun removeEmptyString(file: File) {
    val tempFile = File("${file.path}$TEMP_FILE_NAME_TAIL")
    BufferedReader(FileReader(file)).use { bufferReader ->
        tempFile.bufferedWriter().use { bufferWriter ->
            var line: String?
            var isWriteFirstLine = true
            while (bufferReader.readLine().also { line = it } != null) {
                if (line!!.contains(TARGET_TO_REMOVE)) {
                    continue
                }
                if (!isWriteFirstLine) {
                    bufferWriter.newLine()
                }
                isWriteFirstLine = false
                bufferWriter.write(line)
            }
        }
    }
    file.delete()
    tempFile.renameTo(file)
}