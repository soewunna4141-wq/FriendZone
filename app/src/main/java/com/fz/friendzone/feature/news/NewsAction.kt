package com.fz.friendzone.feature.news

sealed interface NewsAction {

    data object Load : NewsAction
}
