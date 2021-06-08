package bash.sharon.photosApp.network
import android.annotation.SuppressLint
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class PhotosApiService {
    object PhotosApi {
        private const val BASE_URL =
            "https://pixabay.com/api/?key=12175339-7048b7105116d7fa1da74220c"

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()


        val retrofitService: PhotosApiService by lazy {
            retrofit.create(PhotosApiService::class.java)

        }
        @SuppressLint("LongLogTag")
        interface PhotosApiService {
            @GET(" ")
            suspend fun getPhotos(@Query("per_page") perPage: String = "200"): Response<NestedJSONModel>

            @GET(" ")
            suspend fun searchPhoto(
                @Query("per_page") perPage: String = "200",
                @Query("q") input: String,
                @Query("image_type") imageType: String = "photos"
            ):Response<NestedJSONModel>

        }

    }
}
