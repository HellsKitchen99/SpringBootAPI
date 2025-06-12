package com.test;

import com.google.gson.*;
import org.apache.http.HttpHost;
import org.opensearch.client.*;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class OpenSearchService {

    private final RestClient client;
    private final Gson gson = new Gson();

    public OpenSearchService() {
        String host = System.getenv().getOrDefault("OPENSEARCH_HOST", "localhost");
        this.client = RestClient.builder(new HttpHost(host, 9200, "http")).build();
    }
    public List < IBP > getDataFromOpenSearch() {
        Request request = new Request("GET", "/ups-stats/_search");
            request.setJsonEntity("{ \"size\": 10000 }");
        for (int i = 1; i <= 5; i++) {
            System.out.println(">>> попытка подключения к OpenSearch: попытка " + i);
            try {
                List < IBP > IBPlist = new ArrayList < > ();
                Response response = client.performRequest(request);
                String json = new String(response.getEntity().getContent().readAllBytes());

                JsonArray hits = JsonParser.parseString(json)
                    .getAsJsonObject()
                    .getAsJsonObject("hits")
                    .getAsJsonArray("hits");

                for (JsonElement el: hits) {
                    JsonObject src = el.getAsJsonObject().getAsJsonObject("_source");
                    IBP ibp = gson.fromJson(src, IBP.class);
                    IBPlist.add(ibp);
                }
                return IBPlist;
            } catch (ResponseException err) {
                System.out.print("OpenSearch вернул ошибку - " + err);
            } catch (JsonParseException err) {
                System.out.print("Ошибка при парсинге JSON - " + err);
            } catch (IOException err) {
                System.out.print("Сетевая ошибка - " + err);
            }
        }
        System.out.println(">>> не удалось подключиться к OpenSearch после 5 попыток");
        return Collections.emptyList();
    }

}