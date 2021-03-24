package com.huang.remotetransfer;

import com.huang.remotetransfer.base.OpenApiResponse;
import com.huang.remotetransfer.api.RemoteTransferTestApi;
import com.huang.remotetransfer.domain.AddCategoryRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemotetransferApplicationTests {

    @Autowired
    private RemoteTransferTestApi remoteTransferTestApi;

    @Test
    public void query() {
        OpenApiResponse<Map<String, Object>> response = remoteTransferTestApi.queryCategoryByTitle(20, "test", 1, 10);
        System.out.println(response);

    }

    @Test
    public void delete() {
        OpenApiResponse<Void> response = remoteTransferTestApi.deleteCategory(1L);

        System.out.println(response);
    }

    @Test
    public void create() {
        AddCategoryRequest request = new AddCategoryRequest();
        request.setBizid(20);
        request.setCategoryId(1L);
        request.setNameEn("name");
        request.setName("name");
        request.setVisible(1);
        OpenApiResponse<Void> response = remoteTransferTestApi.createCategory(request);
        System.out.println(response);
    }

    @Test
    public void update() {
        AddCategoryRequest request = new AddCategoryRequest();
        request.setBizid(20);
        request.setCategoryId(1L);
        request.setNameEn("name");
        request.setName("name");
        request.setVisible(1);
        OpenApiResponse<Void> response = remoteTransferTestApi.updateCategory(request);
        System.out.println(response);
    }
}
