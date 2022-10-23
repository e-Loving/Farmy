package uz.eloving.farmy.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GardenItemModel(
    val pic: Bitmap,
    val disease: String,
    val confidence: Float,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
