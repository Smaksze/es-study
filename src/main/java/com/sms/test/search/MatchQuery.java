package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
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
 * @date 2020-12-25 16:47
 */
public class MatchQuery {

    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
            查询所有
     */

    @Test
    public void matchAllQuery() throws IOException {
        //创建request
        SearchRequest request = new SearchRequest();
        request.indices(index);
        request.types(type);

        //指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(20);
        builder.query(QueryBuilders.matchAllQuery());
        //填充查询条件
        request.source(builder);

        //使用client执行查询
        SearchResponse response = ESClient.getClient().search(request, RequestOptions.DEFAULT);

        //查看返回结果
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }
        System.out.println(response.getHits().getHits().length);

    }

    /*
            智能查询 ： 如果field为text则分词，反之不分词
     */
    @Test
    public void matchQuery() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest();
        request.indices(index);
        request.types(type);

        //指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("smsContent","严肃"));
        //填充查询条件
        request.source(builder);

        //client执行查询
        SearchResponse response = ESClient.getClient().search(request, RequestOptions.DEFAULT);

        //查看结果
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }

    }

    /*
            一个查询值在多个属性中去查
     */

    @Test
    public void multiMatchQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery("北京","province","smsContent"));

        request.source(builder);

        SearchResponse response = ESClient.getClient().search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }

    }


}
