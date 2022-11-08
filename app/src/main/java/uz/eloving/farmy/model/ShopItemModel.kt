package uz.eloving.farmy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShopItemModel(
    val name: String? = null,
    val price: Float? = null,
    val desc: String? = null,
    val amount: Float? = null,
    val imageDonwloadUrl: String? = null,
    val type: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
