package bash.sharon.photosApp.overview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bash.sharon.photosApp.network.HitsItem
import bash.sharon.photosApp.network.PhotosApiService.PhotosApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.HashMap

enum class ApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {
    private val tagIdMap = HashMap<String, HashSet<Int>>()
    private val idHitsItemMap = HashMap<Int, HitsItem>()


    //  private val _status = MutableLiveData<ApiStatus>()
    // val status: LiveData<ApiStatus> = _status

    // The internal MutableLiveData that stores the status of the most recent request
    val _status = MutableLiveData<String>()

    //private val _photos = MutableLiveData<List<HitsItem>>()

    val photos = MutableLiveData<List<HitsItem>>()
    //val listOfUrl = MutableLiveData<List<String>>()

    // The external immutable LiveData for the request status
    //  val status: LiveData<List<HitsItem>> = _status

    var fullPhotoList = emptyList<HitsItem>()

    init {
        getPhotos()
    }

    companion object {
        const val TAG = "OverviewViewModel"
    }

    object MPhoto

    var instance: List<HitsItem>? = null
    private fun getPhotos() {

        viewModelScope.launch {
            //   _status.value = ApiStatus.LOADING

//            withContext(Dispatchers.IO) {
            try {
                val response = PhotosApi.retrofitService.getPhotos()

                if (!response.isSuccessful) {
                    Log.w(TAG, "getPhotos: failed to get photos ")
                    //   _status.value = ApiStatus.ERROR
                    return@launch
//                        return@withContext
                }

                response.body()?.hits?.let { arrayOfHitsItems ->
                    Log.d(TAG, "getPhotos: $arrayOfHitsItems")
                    fillMaps(arrayOfHitsItems)
                    fullPhotoList = arrayOfHitsItems
                    photos.postValue(arrayOfHitsItems)

                }


            } catch (e: UnknownHostException) {
                _status.value = "there is not internt connection"


            } catch (e: Exception) {
                //genreal error
                _status.value = "some error occurred ${e.message}"

            }
//            }

        }
    }

    private fun fillMaps(hits: List<HitsItem>) {
        idHitsItemMap.putAll(hits.associate { Pair(it.id ?: -1, it) })
        //tagIdMap fill
        //1, take tag from hit item and split it to list
        //2.for each tag from the list it on tagIdMap
        //** notice SET value can be already with ids
        //3. then for each tag put current hit item to list value
        //Map<String,Set<Int>>
        //Map<flower,[1]>
        //Map<bloom,[1]>
        //next input -> HitItem(tag:bloom,amazing2,)
        //Map<bloom,[1>ADD(2)
        hits.forEach {
            it.tagsList.forEach { tag ->
                tagIdMap.getOrPut(tag, { hashSetOf(it.id ?: -1) }).add(it.id ?: -1)
            }
        }

        //

    }
    //check for binding edittext (onCahnge text)
    fun search(input: String?) {
        if (input.isNullOrEmpty()) {
            photos.value = fullPhotoList
            return
        }

        //emit event to photos //
        //todo check thissss !!!!!!!!!!!!!!!!!!!!!
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = PhotosApi.retrofitService.searchPhoto(input = input)
                if (!response.isSuccessful) {
                    Log.w(TAG, "getPhotos: failed to search photos ")
                    //   _status.value = ApiStatus.ERROR
                    return@withContext
                }
                response.body()?.hits?.let { arrayOfHitsItems ->
                    Log.d(TAG, "getPhotos: $arrayOfHitsItems")
                    fillMaps(arrayOfHitsItems)
                    //emit event to photos
                    photos.postValue(arrayOfHitsItems)
                }
            }

        }
    }
}