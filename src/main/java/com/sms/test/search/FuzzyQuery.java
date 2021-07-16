package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-06 9:55
 */
public class FuzzyQuery {
    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
        模糊查询 如阿里旺旺 ---> 啊里旺旺

     */

    @Test
    public void fuzzyQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.fuzzyQuery("corpName","阿狸旺旺").prefixLength(1));

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }



    }

}
