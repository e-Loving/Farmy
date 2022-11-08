package uz.eloving.farmy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.eloving.farmy.model.GardenItemModel
import uz.eloving.farmy.model.ShopItemModel

@Dao
interface ShopDao {
    @Insert
    fun insertItem(shopItemModel: ShopItemModel): Long

    @Query("Select * From ShopItemModel")
    fun getItem(): LiveData<List<ShopItemModel>>

    @Query("Delete from ShopItemModel where id=:uid")
    fun deleteItem(uid: Long)

    @Query("Delete from ShopItemModel")
    fun deleteAll()

}