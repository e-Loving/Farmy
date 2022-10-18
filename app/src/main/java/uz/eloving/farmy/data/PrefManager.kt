package uz.eloving.farmy.data

import android.content.Context
import android.content.SharedPreferences

const val CACHE_NAME = "V-Craft"
const val UID = "uid"
const val SHOP_ITEM_COUNT = "SHOP_ITEM_COUNT"

class PrefManager {
    companion object {
        private fun getInstance(ctx: Context): SharedPreferences {
            return ctx.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE)
        }

        fun getUID(ctx: Context): String? {
            return getInstance(ctx).getString(UID, UID)
        }

        fun saveUID(ctx: Context, uid: String) {
            getInstance(ctx).edit().putString(UID, uid).apply()
        }

        fun getShopItemCount(ctx: Context): Int {
            return getInstance(ctx).getInt(SHOP_ITEM_COUNT, 0)
        }

        fun setShopItemCount(ctx: Context) {
            getInstance(ctx).edit().putInt(SHOP_ITEM_COUNT, getShopItemCount(ctx) + 1).apply()
        }
    }
}