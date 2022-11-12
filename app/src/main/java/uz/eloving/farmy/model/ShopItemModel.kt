package uz.eloving.farmy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShopItemModel(
    val name: String? = null,
    val price: Float? = null,
    val imageDownloadUrl: String? = null,
    val type: String? = null,
    val per: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
