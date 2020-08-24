package com.edgeatzero.library.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.regex.Matcher

fun Matcher.range(): IntRange {
    return IntRange(start(), end())
}

fun Matcher.range(group: Int): IntRange {
    return IntRange(start(group), end(group))
}

@RequiresApi(Build.VERSION_CODES.O)
fun Matcher.range(name: String): IntRange {
    return IntRange(start(name), end(name))
}
