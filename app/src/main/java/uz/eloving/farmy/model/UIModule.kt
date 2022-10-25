package uz.eloving.farmy.model

import java.net.IDN

data class UIModule(
    val title: String,
    val lottie_animation: Int,
    val description: String,
    val nextButtonVisibility: Boolean
)
data class JobModule(
    val id: Int,
    val jobType:String

)
data class InfoModule(
    val id: Int,
    val image:Int,
    val name:String,
    val description: String,
    val like:Int,
    val date:Int
)
