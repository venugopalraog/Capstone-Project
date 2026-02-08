package com.capstone.designpatterntutorial.model.mainscreen

import java.io.Serializable

data class Pattern(
    val id: Int,
    val name: String,
    val summary: String,
    val url: String,
    val type: String,
    val isFavorite: Boolean = false
) : Serializable
