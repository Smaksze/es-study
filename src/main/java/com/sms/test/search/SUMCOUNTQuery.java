package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-07 9:40
 */
public class SUMCOUNTQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
            去重计数
     */
    
    @Test
    public void testCardinalityQuery() throws IOException {

        SearchRequest req = new SearchRequest(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();

        CardinalityAggregationBuilder aggregationBuilder = AggregationBuilders.cardinality("agg").field("province");
        builder.aggregation(aggregationBuilder);

        req.source(builder);

        SearchResponse response = client.search(req, RequestOptions.DEFAULT);

        Cardinality agg = response.getAggregations().get("agg");
        System.out.println(agg.getValue());


    }

    /*
        范围统计
     */
    @Test
    public void testRange() throws IOException {

        SearchRequest request = new SearchRequest(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.aggregation(AggregationBuilders.range("agg").field("fee")
                .addUnboundedTo(5)
                .addRange(5,10)
                .addUnboundedFrom(10)
        );

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        Range agg = response.getAggregations().get("agg");
        agg.getBuckets().forEach(b ->{
            String key = b.getKeyAsString();
            Object from = b.getFrom();
            Object to = b.getTo();
            long docCount = b.getDocCount();
            System.out.println(String.format("key: %s,from: %s, To: %s, docCount: %s",key,from,to,docCount));
        });


    }

}
