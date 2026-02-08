package com.capstone.designpatterntutorial.model.mainscreen

import java.io.Serializable

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    var patternList: ArrayList<Pattern>
) : Serializable
