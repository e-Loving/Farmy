package uz.eloving.farmy.data

import uz.eloving.farmy.R
import uz.eloving.farmy.model.UIModule

class MockData {
    companion object {
        val data = arrayListOf(
            UIModule(
                "Tabiatni birga asraymiz ! ", R.raw.save_nature,
                "Biz sizga kasallangan o'simligingizda yordam beramiz. Qanday deysizmi ?" +
                        "Bizga o'simligingiz rasmini yuklang va sizga shu zahoti kasallik haqida" +
                        "habar beramiz .",
                false
            ),
            UIModule(
                "Online sotib oling ! ", R.raw.online_shop,
                "Mahalliy bozordan mahsulotlarni arzon narxda olishga nima deysiz ?",
                false
            ),
            UIModule(
                "Ish kerakmi yoki ishchi ? ", R.raw.job_offer,
                "Bizda o'zingizga mos ish topishingiz va o'z ishingizga yollanma ishchi " +
                        "jalb qilishingiz mumkin .",
                true
            )
        )
    }
}