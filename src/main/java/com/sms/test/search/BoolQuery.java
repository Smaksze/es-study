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
 * @date 2021-01-06 15:14
 */
public class BoolQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
        复合过滤器：将所有条件以一定逻辑添加到一起

        must：相当于and
        must_not: 相当于not
        should：相当于or

     */

    @Test
    public void boolQuery() throws IOException {

        SearchRequest request = new SearchRequest(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.termsQuery("province","重庆","上海"));
        boolQueryBuilder.must(QueryBuilders.termQuery("corpName","腾讯课堂"));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("smsContent","经验"));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("smsContent","强迫"));
        builder.query(boolQueryBuilder);

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }


    }

}
