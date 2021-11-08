package tardova.online.copybook.model

import com.squareup.moshi.Json

data class WikiEntity(
        val query: WikiQuery
)

data class WikiQuery(
        val pages: Map<String, WikiPage>
)

data class WikiPage(
        val title: String,
        val extract: String
)