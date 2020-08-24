//@file:Suppress("UNCHECKED_CAST")
//
//package com.edgeatzero.library.ext
//
//import android.os.Bundle
//import android.os.Parcelable
//import com.edgeatzero.library.common.BundleDelegate
//import java.io.Serializable
//
//fun (() -> Bundle).serializable(
//    key: String? = null,
//    defValue: Serializable
//): BundleDelegate<Serializable> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getSerializable(p0) ?: p1 },
//    setter = Bundle::putSerializable
//)
//
//fun <T : Parcelable> (() -> Bundle).parcelable(
//    key: String? = null,
//    defValue: T? = null
//): BundleDelegate<T?> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getParcelable(p0) ?: p1 },
//    setter = Bundle::putParcelable
//)
//
//fun <T : Parcelable> (() -> Bundle).parcelableNotNull(
//    key: String? = null,
//    defValue: T
//): BundleDelegate<T> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getParcelable(p0) ?: p1 },
//    setter = Bundle::putParcelable
//)
//
//fun <T : Parcelable> (() -> Bundle).parcelableArray(
//    key: String? = null,
//    defValue: Array<T>?
//): BundleDelegate<Array<T>?> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getParcelableArray(p0) as? Array<T> ?: p1 },
//    setter = Bundle::putParcelableArray
//)
//
//fun <T : Parcelable> (() -> Bundle).parcelableArrayList(
//    key: String? = null,
//    defValue: ArrayList<T> = ArrayList()
//): BundleDelegate<ArrayList<T>> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getParcelableArrayList<T>(p0) ?: p1 },
//    setter = Bundle::putParcelableArrayList
//)
//
//fun (() -> Bundle).byte(
//    key: String? = null,
//    defValue: Byte = 0
//): BundleDelegate<Byte> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getByte,
//    setter = Bundle::putByte
//)
//
//fun (() -> Bundle).byteArray(
//    key: String? = null,
//    defValue: ByteArray = ByteArray(0)
//): BundleDelegate<ByteArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getByteArray(p0) ?: p1 },
//    setter = Bundle::putByteArray
//)
//
//fun (() -> Bundle).char(
//    key: String? = null,
//    defValue: Char = 0.toChar()
//): BundleDelegate<Char> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getChar,
//    setter = Bundle::putChar
//)
//
//fun (() -> Bundle).charArray(
//    key: String? = null,
//    defValue: CharArray = CharArray(0)
//): BundleDelegate<CharArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getCharArray(p0) ?: p1 },
//    setter = Bundle::putCharArray
//)
//
//fun (() -> Bundle).charSequence(
//    key: String? = null,
//    defValue: CharSequence = ""
//): BundleDelegate<CharSequence> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getCharSequence,
//    setter = Bundle::putCharSequence
//)
//
//fun (() -> Bundle).charSequenceArray(
//    key: String? = null,
//    defValue: Array<CharSequence> = emptyArray()
//): BundleDelegate<Array<CharSequence>> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getCharSequenceArray(p0) ?: p1 },
//    setter = Bundle::putCharSequenceArray
//)
//
//fun (() -> Bundle).charSequenceArrayList(
//    key: String? = null,
//    defValue: ArrayList<CharSequence> = ArrayList()
//): BundleDelegate<ArrayList<CharSequence>> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getCharSequenceArrayList(p0) ?: p1 },
//    setter = Bundle::putCharSequenceArrayList
//)
//
//fun (() -> Bundle).string(
//    key: String? = null,
//    defValue: String = ""
//): BundleDelegate<String> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getString,
//    setter = Bundle::putString
//)
//
//fun (() -> Bundle).stringArray(
//    key: String? = null,
//    defValue: Array<String> = emptyArray()
//): BundleDelegate<Array<String>> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getStringArray(p0) ?: p1 },
//    setter = Bundle::putStringArray
//)
//
//fun (() -> Bundle).stringArrayList(
//    key: String? = null,
//    defValue: ArrayList<String> = ArrayList()
//): BundleDelegate<ArrayList<String>> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getStringArrayList(p0) ?: p1 },
//    setter = Bundle::putStringArrayList
//)
//
//fun (() -> Bundle).int(
//    key: String? = null,
//    defValue: Int = 0
//): BundleDelegate<Int> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getInt,
//    setter = Bundle::putInt
//)
//
//fun (() -> Bundle).intArray(
//    key: String? = null,
//    defValue: IntArray = IntArray(0)
//): BundleDelegate<IntArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getIntArray(p0) ?: p1 },
//    setter = Bundle::putIntArray
//)
//
//fun (() -> Bundle).intArrayList(
//    key: String? = null,
//    defValue: ArrayList<Int> = ArrayList()
//): BundleDelegate<ArrayList<Int>> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getIntegerArrayList(p0) ?: p1 },
//    setter = Bundle::putIntegerArrayList
//)
//
//fun (() -> Bundle).long(
//    key: String? = null,
//    defValue: Long = 0L
//): BundleDelegate<Long> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getLong,
//    setter = Bundle::putLong
//)
//
//fun (() -> Bundle).longArray(
//    key: String? = null,
//    defValue: LongArray = LongArray(0)
//): BundleDelegate<LongArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getLongArray(p0) ?: p1 },
//    setter = Bundle::putLongArray
//)
//
//fun (() -> Bundle).float(
//    key: String? = null,
//    defValue: Float = 0F
//): BundleDelegate<Float> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getFloat,
//    setter = Bundle::putFloat
//)
//
//fun (() -> Bundle).floatArray(
//    key: String? = null,
//    defValue: FloatArray = FloatArray(0)
//): BundleDelegate<FloatArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getFloatArray(p0) ?: p1 },
//    setter = Bundle::putFloatArray
//)
//
//fun (() -> Bundle).double(
//    key: String? = null,
//    defValue: Double = 0.0
//): BundleDelegate<Double> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = Bundle::getDouble,
//    setter = Bundle::putDouble
//)
//
//fun (() -> Bundle).doubleArray(
//    key: String? = null,
//    defValue: DoubleArray = DoubleArray(0)
//): BundleDelegate<DoubleArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getDoubleArray(p0) ?: p1 },
//    setter = Bundle::putDoubleArray
//)
//
//fun (() -> Bundle).boolean(
//    key: String? = null,
//    defValue: Boolean = false
//): BundleDelegate<Boolean> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getBoolean(p0, p1) },
//    setter = { p0, p1 -> putBoolean(p0, p1) }
//)
//
//fun (() -> Bundle).booleanArray(
//    key: String? = null,
//    defValue: BooleanArray = BooleanArray(0)
//): BundleDelegate<BooleanArray> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getBooleanArray(p0) ?: p1 },
//    setter = { p0, p1 -> putBooleanArray(p0, p1) }
//)
//
//
//inline fun <reified T> (() -> Bundle).delegate(
//    key: String? = null,
//    defValue: T,
//    crossinline string: T.() -> String,
//    crossinline target: String.() -> T
//): BundleDelegate<T> = BundleDelegate(
//    bundle = this,
//    key = key,
//    defaultValue = defValue,
//    getter = { p0, p1 -> getString(p0, "")?.target() ?: p1 },
//    setter = { p0, p1 -> putString(p0, p1.string()) }
//)
