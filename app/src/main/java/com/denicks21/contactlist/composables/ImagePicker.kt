package com.denicks21.contactlist.composables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.denicks21.contactlist.R
import java.io.ByteArrayOutputStream

@Composable
fun ImagePicker(
    initialImage: Bitmap? = null,
    onPhotoSelected: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    var image by remember { mutableStateOf(initialImage) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                    val compressedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true)
                    val outputStream = ByteArrayOutputStream()
                    compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                    val compressedData = outputStream.toByteArray()
                    val compressedImage =
                        BitmapFactory.decodeByteArray(compressedData, 0, compressedData.size)
                    image = compressedImage
                    onPhotoSelected(compressedImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Shoes photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.github_logo),
                contentDescription = "Add photo",
                modifier = Modifier
                    .size(100.dp)
                    .clickable { launcher.launch("image/*") }
            )
        }
    }
}