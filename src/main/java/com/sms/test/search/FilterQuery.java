package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-06 16:52
 */
public class FilterQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
        fliter 和 query区别：
            query：根据查询条件，去计算文档的匹配度得到一个分数，根据分数进行排序，不会做缓存。 一般较慢
            fliter： 根据查询条件去查询文档，但是不会计算分数，经常被过滤的数据会进行缓存。一般较快
     */

    @Test
    public void testFliter() throws IOException {
        SearchRequest request = new SearchRequest(index).types(type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("province","重庆"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("fee").lte(50));
        sourceBuilder.query(boolQueryBuilder);

        request.source(sourceBuilder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }

    }
}
