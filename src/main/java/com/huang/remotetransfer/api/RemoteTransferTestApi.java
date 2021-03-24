package com.huang.remotetransfer.api;

import com.huang.remotetransfer.annotation.RemoteTransfer;
import com.huang.remotetransfer.base.OpenApiResponse;
import com.huang.remotetransfer.domain.AddCategoryRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 测试使用@RemoteTransfer注解
 *
 * @Author: huangyichun
 * @Date: 2021/3/24
 */
@Component
@RemoteTransfer(hostName = "TRADE-PLATFORM")
public interface RemoteTransferTestApi {


    @GetMapping("/v1/manage/category/getPageCategoryEx")
    OpenApiResponse<Map<String, Object>> queryCategoryByTitle(@RequestParam(value = "bizid") Integer bizid,
                                                              @RequestParam(value = "name") String name,
                                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                              @RequestParam(value = "pageSize", required = false) Integer pageSize);


    @DeleteMapping("/v1/manage/category/{categoryId}")
    OpenApiResponse<Void> deleteCategory(@PathVariable("categoryId") Long categoryId);

    @PutMapping("/v1/manage/category")
    OpenApiResponse<Void> updateCategory(@RequestBody AddCategoryRequest activityCategory);

    @PostMapping("/v1/manage/category")
    OpenApiResponse<Void> createCategory(@RequestBody AddCategoryRequest activityCategory);
}
