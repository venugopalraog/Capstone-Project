package com.capstone.designpatterntutorial.views.compose

import android.os.Bundle
import androidx.navigation.NavType
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

val PatternNavType: NavType<Pattern> = object : NavType<Pattern>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Pattern? {
        return bundle.getSerializable(key) as Pattern?
    }

    override fun parseValue(value: String): Pattern {
        val decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8.toString())
        return Gson().fromJson(decodedValue, Pattern::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Pattern) {
        bundle.putSerializable(key, value)
    }
}
