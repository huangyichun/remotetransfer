package com.huang.remotetransfer.controller;

import com.alibaba.fastjson.JSON;
import com.huang.remotetransfer.base.OpenApiResponse;
import com.huang.remotetransfer.domain.AddCategoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供4个接口作为remoteTransferTestApi远程调用的接口，由于是案例这里就不创建新项目提供了
 * @Author: huangyichun
 * @Date: 2021/3/24
 */
@Slf4j
@RestController
public class RemoteTransferController {


    @GetMapping("/v1/manage/category/getPageCategoryEx")
    OpenApiResponse<Map<String, Object>> queryCategoryByTitle(@RequestParam(value = "bizid") Integer bizid,
                                                               @RequestParam(value = "name") String name,
                                                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("bizId", bizid);
        map.put("name", name);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageNum);

        return OpenApiResponse.<Map<String, Object>>builder().result(map).build();
    }


    @DeleteMapping("/v1/manage/category/{categoryId}")
    OpenApiResponse<Void> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        log.info("categoryId={}", categoryId);
        return OpenApiResponse.<Void>builder().build();
    }

    @PutMapping("/v1/manage/category")
    OpenApiResponse<Void> updateCategory(@RequestBody AddCategoryRequest activityCategory) {
        log.info("activityCategory info ={}", JSON.toJSONString(activityCategory));
        return OpenApiResponse.<Void>builder().build();
    }

    @PostMapping("/v1/manage/category")
    OpenApiResponse<Void> createCategory(@RequestBody AddCategoryRequest activityCategory){
        log.info("activityCategory info ={}", JSON.toJSONString(activityCategory));
        return OpenApiResponse.<Void>builder().build();
    }

}
