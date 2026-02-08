package com.capstone.designpatterntutorial.model.mainscreen

import java.io.Serializable

data class MainScreenData(
    var categoryList: ArrayList<Category> = ArrayList()
) : Serializable
