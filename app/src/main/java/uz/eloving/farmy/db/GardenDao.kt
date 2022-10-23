package uz.eloving.farmy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.eloving.farmy.model.GardenItemModel

@Dao
interface GardenDao {
    @Insert
    fun insertItem(gardenModel: GardenItemModel): Long

    @Query("Select * From GardenItemModel")
    fun getItem(): LiveData<List<GardenItemModel>>

    @Query("Delete from GardenItemModel where id=:uid")
    fun deleteItem(uid: Long)

    @Query("Delete from GardenItemModel")
    fun deleteAll()
}