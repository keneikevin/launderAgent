package com.example.launder.data
import androidx.paging.PagingSource
import com.example.launder.data.entities.Service
import com.example.launder.data.other.Constants.SERVICE_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CakePagingSource(
    private val db: FirebaseFirestore,
      private val uid: String
) : PagingSource<QuerySnapshot, Service>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Service> {
        return try {
            val currentPage = params.key ?: db.collection(SERVICE_COLLECTION)
                .whereEqualTo("authorUid", uid)
                .get()
                .await()

            val lastDocumentSnapShot = currentPage.documents[currentPage.size() - 1]
            val nextPage = db.collection(SERVICE_COLLECTION)
                .whereEqualTo("authorUid", uid)

                .startAfter(lastDocumentSnapShot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(Service::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}














