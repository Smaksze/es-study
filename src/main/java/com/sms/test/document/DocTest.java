package com.sms.test.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.entity.Person;
import com.sms.util.ESClient;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2020-12-25 9:52
 */
public class DocTest {

    ObjectMapper mapper = new ObjectMapper();
    String index = "person";
    String type = "man";

    /*
        添加文档
     */
    @Test
    public void createDoc() throws IOException {
        //准备json数据
        Person person = new Person();
        person.setName("张三");
        person.setAge(22);
        person.setBirth(new Date());
        String value = mapper.writeValueAsString(person);

        //准备request对象封装数据
        IndexRequest request = new IndexRequest(index, type, "1");
        request.source(value, XContentType.JSON);

        //通过client执行
        IndexResponse response = ESClient.getClient().index(request, RequestOptions.DEFAULT);

        //查看返回结果
        DocWriteResponse.Result result = response.getResult();
    }

    /*
     获取文档，查看是否存在
     */
    @Test
    public void isExist() throws IOException {
        GetRequest getRequest = new GetRequest(index, type, "1");
        GetResponse response = ESClient.getClient().get(getRequest, RequestOptions.DEFAULT);

        boolean exists = response.isExists();
        System.out.println(exists);
    }

    /*
    获得文档信息
     */
    @Test
    public void getDoc() throws IOException {

        GetRequest getRequest = new GetRequest(index, type, "1");
        GetResponse response = ESClient.getClient().get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        System.out.println(sourceAsString);
    }

    /*
    更新文档
     */
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, type, "1");
        Person person = new Person("里斯", 18, new Date());

        String string = mapper.writeValueAsString(person);
        updateRequest.doc(string, XContentType.JSON);

        UpdateResponse response = ESClient.getClient().update(updateRequest, RequestOptions.DEFAULT);

    }

    /*
    删除文档
     */
    @Test
    public void delDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, "1");
        DeleteResponse deleteResponse = ESClient.getClient().delete(deleteRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = deleteResponse.getResult();
    }

    /*
    批量插入
     */
    @Test
    public void bulkDoc() throws IOException {

        BulkRequest bulkRequest = new BulkRequest();

        ArrayList<Person> list = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            IndexRequest indexRequest = new IndexRequest(index, type, i + "").source(mapper.writeValueAsString(list.get(i)), XContentType.JSON);
            bulkRequest.add(indexRequest);
            BulkResponse bulkResponse = ESClient.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            boolean hasFailures = bulkResponse.hasFailures();
            System.out.println(hasFailures);
        }

    }
}
