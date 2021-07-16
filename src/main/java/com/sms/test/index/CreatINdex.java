package com.sms.test.index;

import com.sms.util.ESClient;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2020-12-24 17:31
 */
public class CreatINdex {

    String index = "person";
    String type = "man";
    static RestHighLevelClient client = null;
    static {
        client = ESClient.getClient();
    }

    @Test
    public void testCreat() throws IOException {
        //1.准备settings
        Settings.Builder settings = Settings.builder().put("number_of_shards", 3).put("number_of_replicas", 1);

        //2准备mappings
        XContentBuilder mapping = JsonXContent.contentBuilder()
                .startObject()
                    .startObject("properties")
                        .startObject("name")
                            .field("type","text")
                        .endObject()
                        .startObject("age")
                            .field("type","integer")
                        .endObject()
                        .startObject("birth")
                            .field("type","date")
                            .field("format","yyyy-MM-dd")
                        .endObject()
                    .endObject()
                .endObject();

        //3准备request装载setting和mapping
        CreateIndexRequest request = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(type,mapping);

        //4通过客户端连接es并执行创建索引
        CreateIndexResponse response
                = ESClient.getClient().indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());


    }

}
