package com.edgeatzero.projects.pixiv.database

import androidx.room.*
import com.edgeatzero.library.ext.fromJson
import com.edgeatzero.library.ext.toJson
import com.edgeatzero.projects.pixiv.model.AuthTokenData
import com.edgeatzero.projects.pixiv.model.util.BindingUtils

@TypeConverters(AccountEntity.TypeConverters::class)
@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") override val id: Long,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "account") var account: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "device_token") var device_token: String = "",
    @ColumnInfo(name = "refresh_token") var refresh_token: String = "",
    @ColumnInfo(name = "user_info") var user: AuthTokenData.User = AuthTokenData.User()
) : BindingUtils.IdHolder {

    constructor(data: AuthTokenData, password: String): this(
        id = data.response.user.id,
        name = data.response.user.name,
        email = data.response.user.mailAddress,
        account = data.response.user.account,
        password = password,
        device_token = data.response.deviceToken,
        refresh_token = data.response.refreshToken,
        user = data.response.user
    )

    fun update(data: AuthTokenData? = null, password: String? = null): AccountEntity {
        if (data != null) {
            name = data.response.user.name
            email = data.response.user.mailAddress
            account = data.response.user.account
            device_token = data.response.deviceToken
            refresh_token = data.response.refreshToken
            user = data.response.user
        }
        if (password != null) {
            this.password = password
        }
        return this
    }

    class TypeConverters {

        @TypeConverter
        fun storeUserToString(value: AuthTokenData.User): String = value.toJson()

        @TypeConverter
        fun storeStringToUser(value: String): AuthTokenData.User = value.fromJson()

    }

}
