package com.capstone.designpatterntutorial.services

import android.content.Intent
import android.os.Build
import java.io.Serializable

@Suppress("DEPRECATION")
fun <T : Serializable> Intent.getSerializable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, clazz)
    } else {
        getSerializableExtra(key) as? T
    }
}
