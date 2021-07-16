package com.sms.util;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import java.awt.print.Book;
import java.io.IOException;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2020-12-24 16:51
 */
public class ESClient {

    public static RestHighLevelClient getClient(){

        HttpHost host = new HttpHost("192.168.190.99",9200);
        RestClientBuilder builder = RestClient.builder(host);
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;

    }

}
