package bash.sharon.photosApp.network

import com.squareup.moshi.Json

data class NestedJSONModel(
    @field:Json(name = "hits")
    val hits: List<HitsItem>?
)

data class HitsItem(

    @Json(name="webformatURL")
    val webformatURL: String? = null,
    @Json(name="userImageURL")
    val userImageURL: String? = null,
    @Json(name="previewURL")
    val previewURL: String? = null,
    @Json(name="type")
    val type: String? = null,
    @Json(name = "tags")
    val tags: String? = null,
    @Json(name = "user_id")
    val userId: Int? = null,

    @Json(name = "largeImageURL")
    val largeImageURL: String? = null,
    @Json(name = "pageURL")
    val pageURL: String? = null,
    @Json(name = "id")
    val id: Int? = null
) {   //run time var
    val tagsList get() = tags?.split(",")?.map { it.trim() } ?: listOf()
}