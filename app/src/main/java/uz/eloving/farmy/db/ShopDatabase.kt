package uz.eloving.farmy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.eloving.farmy.model.GardenItemModel
import uz.eloving.farmy.model.ShopItemModel
import uz.eloving.farmy.util.Convertor

@Database(entities = [ShopItemModel::class], version = 1, exportSchema = false)
@TypeConverters(Convertor::class)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: ShopDatabase? = null

        fun getDatabase(context: Context): ShopDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopDatabase::class.java,
                    "shop.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
