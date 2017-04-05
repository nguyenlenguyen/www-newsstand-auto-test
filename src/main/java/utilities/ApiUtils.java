package utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiUtils {
    private String apiUrl;
    private JsonObject jsonObject;

    public ApiUtils(String apiUrl) {
        this.apiUrl = System.getProperty("apiHost") + apiUrl;
        this.jsonObject = new JsonObject();
    }

    public void addParam(String param, String value) {
        this.jsonObject.addProperty(param, value);
    }

    public void addParam(String param, int value) {
        this.jsonObject.addProperty(param, value);
    }

    private JsonObject getJsonFromResponse(HttpResponse httpResponse) {
        try {
            return new JsonParser().parse(new InputStreamReader(httpResponse.getEntity().getContent())).getAsJsonObject();
        } catch (Exception ex) {
            return null;
        }
    }

    public JsonObject sendGET() {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(this.apiUrl).openConnection();
            request.connect();
            return new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        } catch (Exception ex) {
            return null;
        }
    }

    public JsonObject sendPUT() {
        try {
            HttpPut request = new HttpPut(this.apiUrl);
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            return getJsonFromResponse(HttpClientBuilder.create().build().execute(request));
        } catch (Exception ex) {
            return null;
        }
    }

    public JsonObject sendPOST() {
        try {
            HttpPost request = new HttpPost(this.apiUrl);
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            return getJsonFromResponse(HttpClientBuilder.create().build().execute(request));
        } catch (Exception ex) {
            return null;
        }
    }

    public JsonObject sendDELETE() {
        try {
            HttpDelete request = new HttpDelete(this.apiUrl);
            request.addHeader("content-type", "application/json");
            return getJsonFromResponse(HttpClientBuilder.create().build().execute(request));
        } catch (Exception ex) {
            return null;
        }
    }
}