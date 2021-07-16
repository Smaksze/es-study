package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2020-12-25 17:23
 */
public class IdQuery {

    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    @Test
    public void idQuery() throws IOException {
        GetRequest request = new GetRequest(index, type, "1");

        GetResponse response = ESClient.getClient().get(request, RequestOptions.DEFAULT);

        Map<String, Object> source = response.getSource();
        System.out.println(source);

    }

    @Test
    public void idsQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.idsQuery().addIds("10","15"));
        request.source(builder);

        SearchResponse response = ESClient.getClient().search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }

    }
}
