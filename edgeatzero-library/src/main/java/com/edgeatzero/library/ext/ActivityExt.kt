package com.edgeatzero.library.ext

import android.app.Activity
import android.os.Bundle

val Activity.bundleProducer: () -> Bundle
    get() = { intent.extras ?: Bundle.EMPTY }
