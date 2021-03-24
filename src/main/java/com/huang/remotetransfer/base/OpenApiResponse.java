package com.huang.remotetransfer.base;

import lombok.Data;

import java.io.Serializable;

/**
 * jcloud api gateway open api standard response object
 *
 * @author wangjiehong
 * @date 2019/9/20 16:32
 */
@Data
public class OpenApiResponse<T> implements Serializable {

    private String requestId;
    private ServiceError error;
    private T result;


    public static <T> OpenApiResponseBuilder<T> builder() {
        return new OpenApiResponseBuilder<> ();
    }

    public static class OpenApiResponseBuilder<T> {
        private String requestId;
        private ServiceError error;
        private T result;

        private OpenApiResponseBuilder() {
        }

        public OpenApiResponseBuilder<T> requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public OpenApiResponseBuilder<T>  error(ServiceError error) {
            this.error = error;
            return this;
        }

        public OpenApiResponseBuilder<T>  result(T result) {
            this.result = result;
            return this;
        }

        public OpenApiResponse<T> build() {
            OpenApiResponse<T>  openApiResponse = new OpenApiResponse<>();
            openApiResponse.requestId = requestId;
            openApiResponse.error = error;
            openApiResponse.result = result;
            return openApiResponse;
        }
    }
}
