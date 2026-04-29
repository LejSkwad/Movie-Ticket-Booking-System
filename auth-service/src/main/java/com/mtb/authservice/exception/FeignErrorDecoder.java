package com.mtb.authservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtb.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response){
        // methodKey = tên method Feign đang gọi
        try {
            ErrorResponse error = objectMapper.readValue(
                    response.body().asInputStream(), //đọc body dạng stream
                    ErrorResponse.class);            // parse vào ErrorResponse
            return new ServiceException(
                    response.status(),
                    error.getError(),
                    error.getMessage());
        } catch(IOException e){
            // body rỗng hoặc không thực hiện parse -> trả lỗi chung
            return new ServiceException(response.status(), "UNKNOWN", "Lỗi kết nối nội bộ");
        }
    }
}
