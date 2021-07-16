package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-06 10:19
 */
public class RangeQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";
    
    /*
       范围查询， 只针对数值型
    
     */
    
    @Test
    public void testRangeQuery() throws IOException {
        SearchRequest request = new SearchRequest(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.rangeQuery("fee").gte(60).lte(100));

        request.source(builder);

        for (SearchHit hit : client.search(request, RequestOptions.DEFAULT).getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }


    }

}
