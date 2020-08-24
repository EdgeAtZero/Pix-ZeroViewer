package com.edgeatzero.projects.pixiv.event

import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.User

data class RefreshEvent(val contentType: ContentType, val id: Long, val boolean: Boolean) {

    constructor(illustration: Illustration) : this(
        if (illustration.isManga) ContentType.MANGA else ContentType.ILLUSTRATION,
        illustration.id,
        illustration.isLiked
    )

    constructor(novel: Novel) : this(
        ContentType.NOVEL,
        novel.id,
        novel.isLiked
    )

    constructor(user: User) : this(
        ContentType.USER,
        user.id,
        user.isFollowed
    )

}
