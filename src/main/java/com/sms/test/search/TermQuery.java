package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2020-12-25 14:54
 */
public class TermQuery {

    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";


    /*

      不对查询条件进行分词，
                term 和 terms的区别
            term相当于 province = "北京"、
            terms相当于 province = "北京" or province= ""

     */


    @Test
    public void termQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.types(type);
        request.indices(index);

        //构造searchSource
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        searchSource.size(5);
        searchSource.from(0);
        searchSource.query(QueryBuilders.termQuery("province","北京"));

        //填充source
        request.source(searchSource);

        //使用client执行查询
        SearchResponse response = ESClient.getClient().search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }



    @Test
    public void termsQuery() throws IOException {

        //创建request
        SearchRequest request = new SearchRequest();
        request.types(type);
        request.indices(index);

        //创建查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("smsContent","锻炼","少年"));
        //
        request.source(builder);

        //执行查询
        SearchResponse response = ESClient.getClient().search(request, RequestOptions.DEFAULT);

        //查看结果
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }

    }



}
