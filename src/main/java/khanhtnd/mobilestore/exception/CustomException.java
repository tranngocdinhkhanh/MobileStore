package khanhtnd.mobilestore.exception;

import khanhtnd.mobilestore.utils.Message;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final Message description;

    public CustomException(Message description) {
        super(description.getDescription());
        this.description = description;
    }
}
