package org.d3if3091.tempconverter.model

data class TempType(
    val name:String,
    val titleResId:Int,
    val symbolResId:Int,
    val formula:Map<String,(Float)->Float>
)
