package org.d3if3091.tempconverter.ui.screen

import androidx.lifecycle.ViewModel
import org.d3if3091.tempconverter.R
import org.d3if3091.tempconverter.model.TempType

class MainViewModel:ViewModel() {
    val tempType:Map<String,TempType> = getTypeTempMap()
    private fun getTypeTempMap():Map<String,TempType>{
        return mutableMapOf<String,TempType>(
            "celsius" to TempType("celsius", R.string.celsius,R.string.celsius_symbol, mapOf(
                "fahrenheit" to { celsius -> (9f*celsius/5f)+32f},
                "celsius" to { celsius -> celsius },
                "kelvin" to { celsius -> celsius+273.15f },
                "reamur" to { celsius -> (4f*celsius/5f) }
            )),
            "reamur" to TempType("reamur", R.string.reamur,R.string.reamur_symbol, mapOf(
                "fahrenheit" to { reamur -> (9f*reamur/4f)+32f},
                "celsius" to { reamur -> (5f*reamur/4f) },
                "kelvin" to { reamur -> (5f*reamur/4f)+273.15f },
                "reamur" to { reamur -> reamur }
            )),
            "fahrenheit" to TempType("fahrenheit", R.string.fahrenheit,R.string.fahrenheit_symbol, mapOf(
                "fahrenheit" to { fahrenheit -> fahrenheit},
                "celsius" to { fahrenheit -> 5f*(fahrenheit-32f)/9f  },
                "kelvin" to { fahrenheit -> (5f*(fahrenheit-32f)/9f) + 273.15f },
                "reamur" to { fahrenheit -> (4f*(fahrenheit-32f))/9f }
            )),
            "kelvin" to TempType("kelvin", R.string.kelvin,R.string.kelvin_symbol, mapOf(
                "fahrenheit" to { kelvin -> (9f*(kelvin-273f)/5f) +32f},
                "celsius" to { kelvin -> kelvin-273.15f  },
                "kelvin" to { kelvin -> kelvin },
                "reamur" to { kelvin -> 4f*(kelvin-732f)/5f  }
            )),
        )
    }
}