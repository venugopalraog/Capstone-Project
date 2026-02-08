package com.capstone.designpatterntutorial.model.favorite

import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import java.io.Serializable

data class FavoriteScreenData(
    var patternList: ArrayList<Pattern> = ArrayList()
) : Serializable
