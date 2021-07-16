package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-07 9:12
 */
public class HighLightQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";

    /*
        highlight : 和query同级别,由searchSourceBuild调用
     */


    @Test
    public void testHighLight() throws IOException {
        SearchRequest request = new SearchRequest(index).types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("smsContent","乌"));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("smsContent",10);
        highlightBuilder.preTags("<font color='red'>").postTags("</font>");
        builder.highlighter(highlightBuilder);

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        ArrayList<Map<String, Object>> list = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField smsContent = highlightFields.get("smsContent");


            Map<String, Object> source = hit.getSourceAsMap();
            if (smsContent != null){
                Text[] texts = smsContent.fragments();
                String newSmsContent = "";
                for (Text text : texts){
                    newSmsContent += text;
                }
                source.put("smsContent",newSmsContent);//把原来的非高亮字段换成新的高亮字段
            }

            list.add(source);
        }
    }


}
