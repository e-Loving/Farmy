package uz.eloving.farmy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.eloving.farmy.model.GardenItemModel
import uz.eloving.farmy.util.Convertor

@Database(entities = [GardenItemModel::class], version = 1, exportSchema = false)
@TypeConverters(Convertor::class)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao

    companion object {
        @Volatile
        private var INSTANCE: GardenDatabase? = null

        fun getDatabase(context: Context): GardenDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GardenDatabase::class.java,
                    "garden.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
