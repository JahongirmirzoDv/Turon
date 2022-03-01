package com.example.turon.utils

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.inputmethod.InputMethodManager
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun closeKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getImageFile(context: Context, dir: File? = null, extension: String? = null): File? {
        try {
            // Create an image file name
            val ext = extension ?: ".jpg"
            val imageFileName = "IMG_${getTimestamp()}$ext"

            // Create File Directory Object
            val storageDir = dir ?: getCameraDirectory(context)

            // Create Directory If not exist
            if (!storageDir.exists()) storageDir.mkdirs()

            // Create File Object
            val file = File(storageDir, imageFileName)

            // Create empty file
            file.createNewFile()

            return file
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    @JvmStatic
    fun getFileInfo(context: Context, uri: Uri?): String {
        if (uri == null) {
            return "Image not found"
        }

        // Get Resolution
        val resolution = getImageResolution(context, uri)

        // File Path
        val filePath = FileUriUtils.getRealPath(context, uri)
        val document = getDocumentFile(context, uri) ?: return "Image not found"

        // Get Last Modified
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val modified = sdf.format(document.lastModified())

        // File Size
        val fileSize = getFileSize(document.length())

        return StringBuilder()

            .append("Resolution: ")
            .append("${resolution.first}x${resolution.second}")
            .append("\n\n")

            .append("Modified: ")
            .append(modified)
            .append("\n\n")

            .append("File Size: ")
            .append(fileSize)
            .append("\n\n")

            /*.append("File Name: ")
            .append(getFileName(context.contentResolver, uri))
            .append("\n\n")*/

            .append("File Path: ")
            .append(filePath)
            .append("\n\n")

            .append("Uri Path: ")
            .append(uri.toString())
            .toString()
    }


    @JvmStatic
    fun getFileInfo(file: File?): String {
        if (file == null || !file.exists()) {
            return "Image not found"
        }

        val resolution = getImageResolution(file)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val modified = sdf.format(file.lastModified())
        return StringBuilder()

            .append("Resolution: ")
            .append("${resolution.first}x${resolution.second}")
            .append("\n\n")

            .append("Modified: ")
            .append(modified)
            .append("\n\n")

            .append("File Size: ")
            .append(getFileSize(file))
            .append("\n\n")

            .append("File Path: ")
            .append(file.absolutePath)
            .toString()
    }

    private fun getFileSize(file: File): String {
        val fileSize = file.length().toFloat()
        val mb = fileSize / (1024 * 1024)
        val kb = fileSize / (1024)

        return if (mb > 1) {
            "$mb MB"
        } else {
            "$kb KB"
        }
    }

    private fun getFileSize(fileSize: Long): String {
        val mb = fileSize / (1024 * 1024)
        val kb = fileSize / (1024)

        return if (mb > 1) {
            "$mb MB"
        } else {
            "$kb KB"
        }
    }

    private fun getImageResolution(context: Context, uri: Uri): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val stream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(stream, null, options)
        return Pair(options.outWidth, options.outHeight)
    }

    private fun getImageResolution(file: File): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        return Pair(options.outWidth, options.outHeight)
    }

    private fun getDocumentFile(context: Context, uri: Uri): DocumentFile? {
        var file: DocumentFile? = null
        if (isFileUri(uri)) {
            val path = FileUriUtils.getRealPath(context, uri)
            if (path != null) {
                file = DocumentFile.fromFile(File(path))
            }
        } else {
            file = DocumentFile.fromSingleUri(context, uri)
        }
        return file
    }

    private fun getCameraDirectory(context: Context): File {
        val dir =
            context.getExternalFilesDir(Environment.DIRECTORY_DCIM) // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        return File(dir, "Camera")
    }

    private fun getTimestamp(): String {
        val timeFormat = "yyyyMMdd_HHmmssSSS"
        return SimpleDateFormat(timeFormat, Locale.getDefault()).format(Date())
    }

    private fun isFileUri(uri: Uri): Boolean {
        return "file".equals(uri.scheme, ignoreCase = true)
    }

}

typealias SingleBlock <T> = (T) -> Unit