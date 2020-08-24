package com.edgeatzero.library.common

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

abstract class TagHolder(val TAG: String) {

    constructor(kClass: KClass<*>) : this(kClass.simpleName?:kClass.jvmName)

}
