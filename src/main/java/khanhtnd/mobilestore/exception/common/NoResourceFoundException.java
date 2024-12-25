package khanhtnd.mobilestore.exception.common;

import org.springframework.http.HttpMethod;

public class NoResourceFoundException extends RuntimeException {
    private String httpMethod; // Lưu tên phương thức HTTP dưới dạng chuỗi

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod.name(); // Chuyển HttpMethod thành chuỗi
    }
}
