package com.elian.computeit.core.util

// From: https://github.com/philipplackner/SocialNetworkTwitch/blob/development/app/src/main/java/com/plcoding/socialnetworktwitch/core/util/Resource.kt
typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, val uiText: UiText? = null)
{
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(uiText: UiText? = null, data: T? = null) : Resource<T>(data, uiText)
}