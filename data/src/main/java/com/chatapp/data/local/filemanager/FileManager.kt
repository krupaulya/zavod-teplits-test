package com.chatapp.data.local.filemanager

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri

interface FileManager {

    fun getBase64FromUri(contentResolver: ContentResolver, uri: Uri): String?

    fun base64ToBitmap(base64String: String?): Bitmap?
}