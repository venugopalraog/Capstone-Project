package com.capstone.designpatterntutorial.model.mainscreen

import java.io.Serializable

data class Pattern(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val summary: String,
    val url: String,
    val imageName: String,
    var isFavorite: Boolean = false
) : Serializable
