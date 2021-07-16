package com.sms.test.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.util.ESClient;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2021-01-06 11:51
 */
public class ScrollQuery {

    private RestHighLevelClient client = ESClient.getClient();
    ObjectMapper mapper = new ObjectMapper();
    String type = "sms-logs-type";
    String index = "sms-logs-index";


    /*
        深分页，与 from+size的区别：
        form+size 每次都会查询出所有的内容、排序，再通过文档id展示
        深分页 ：一次查询完所有的内容，然后存放在ES上下文中，查询下一页时只需传入scrollID去内存中检索即可即可。

     */

    @Test
    public void testScorllQuery() throws IOException {
        //创建searchRequest
        SearchRequest request = new SearchRequest(index).types(type);

        //指定scroll信息
        request.scroll(TimeValue.timeValueHours(1L));

        //指定查询条件,和查询数量、排序方法
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(4);
        builder.sort("fee", SortOrder.DESC);
        builder.query(QueryBuilders.matchAllQuery());
        request.source(builder);

        //得到返回结果和scrollID
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        String scrollId = response.getScrollId();
        System.out.println("---------------首页----------------");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }

        while (true){
            //创建SearchScrollRequest
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);

            //指定scroll信息
            scrollRequest.scroll(TimeValue.timeValueHours(1L));

            SearchHit[] hits = client.scroll(scrollRequest,RequestOptions.DEFAULT).getHits().getHits();

            //循环遍历
            if(hits==null || hits.length==0){
                //判断没有数据后，跳出循环
                System.out.println("---------------结束----------------");
                break;
            }else {
                System.out.println("---------------下一页----------------");
                for (SearchHit hit : hits) {
                    System.out.println(hit.getSourceAsMap());
                }
            }

        }

        //创建ClearScrollRequest删除scrollid
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        //指定scrolID
        clearScrollRequest.addScrollId(scrollId);
        //删除scollID
        boolean succeeded = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT).isSucceeded();
        if(succeeded)
            System.out.println("---------------已成功删除----------------");

    }
}
