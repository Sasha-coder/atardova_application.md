package tardova.online.copybook.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tardova.online.copybook.model.WikiEntity

interface Wiki {
    @GET ("/w/api.php?format=json&action=query&prop=extracts&explaintext=1")
    suspend fun search(@Query("titles")definition: String) : Response<WikiEntity>

    @GET("/w/api.php?action=opensearch&limit=1&namespace=0&format=json")
    suspend fun searchUrl(@Query("search") theme: String) : Response<String?>
}