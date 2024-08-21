package com.chatapp.data.local.filemanager

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FileManagerImpl @Inject constructor() : FileManager {

    override fun getBase64FromUri(contentResolver: ContentResolver, uri: Uri): String? {
        return try {
            val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

            val resizedBitmap = bitmap?.let { resizeBitmap(it) }

            val compressedBitmap = resizedBitmap?.let { compressBitmap(it) }

            compressedBitmap?.let {
                val outputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val byteArray = outputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun base64ToBitmap(base64String: String?): Bitmap? {
        return try {
            base64String?.let {
                val byteArray = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int = 800, maxHeight: Int = 600): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleFactor = (maxWidth.toFloat() / width.toFloat()).coerceAtMost(maxHeight.toFloat() / height.toFloat())

        val scaledWidth = (scaleFactor * width).toInt()
        val scaledHeight = (scaleFactor * height).toInt()

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int = 70): Bitmap {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}