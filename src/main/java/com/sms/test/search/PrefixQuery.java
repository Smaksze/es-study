package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-06 9:41
 */
public class PrefixQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
        前缀查询 ： 通过一个关键字去指定一个field的前缀 ，从而查到指定的文档

     */

    @Test
    public void prefixQuery() throws IOException {

        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.prefixQuery("corpName","腾讯"));

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        System.out.println(response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }


    }

}
