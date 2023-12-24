package me.bnnq.messenger.utils

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.bnnq.messenger.serializers.DateSerializer
import me.bnnq.messenger.services.ServerCommunicationPool
import org.json.JSONObject
import java.util.Date

class ServerRequestBuilder(
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateSerializer())
        .create())
{

    private var url: String = ""
    private var body: JSONObject? = null
    private var requestType: Int = Request.Method.GET
    private var jsonResponseListener: ((response: JSONObject) -> Unit)? = null
    private var stringResponseListener: ((response: String) -> Unit)? = null
    private var errorListener: ((error: VolleyError) -> Unit)? = null
    private var credentialsIncluded: Boolean = false
    private var actionIdentifier: String? = null

    fun <T> setBody(body: T) =
        apply {
            this.body = JSONObject(gson.toJson(body))
        }

    fun setUrl(url: String) =
        apply {
            this.url = url
        }

    fun setMethod(method: Int) =
        apply {
            this.requestType = method
        }

    fun setJsonResponseListener(listener: (response: JSONObject) -> Unit) =
        apply {
            this.jsonResponseListener = listener
        }

    fun setStringResponseListener(listener: (response: String) -> Unit) =
        apply {
            this.stringResponseListener = listener
        }

    fun setErrorListener(listener: (error: VolleyError) -> Unit) =
        apply {
            this.errorListener = listener
        }

    fun withCredentials() =
        apply {
            this.credentialsIncluded = true
        }

    fun withActionIdentifier() =
        apply {
            this.actionIdentifier = ServerCommunicationPool.getNewActionIdentifier()
        }

    val builder: () -> Request<*>
        get() = {
            val headers = mutableMapOf<String, String>().apply {
                if (credentialsIncluded)
                {
                    this["Authorization"] = ServerCommunicationPool.getAuthenticationHeader()
                }
                actionIdentifier?.let {
                    this["ActionIdentifier"] = it
                }
            }

            if (body != null)
            {
                object : JsonObjectRequest(requestType, url, body, jsonResponseListener, errorListener)
                {
                    override fun getHeaders(): Map<String, String> =
                        headers
                }
            }
            else
            {
                object : StringRequest(requestType, url, stringResponseListener, errorListener)
                {
                    override fun getHeaders(): Map<String, String> =
                        headers
                }
            }.also { request ->
                request.retryPolicy = DefaultRetryPolicy(
                    50000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            }
        }


}
