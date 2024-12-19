package khanhtnd.mobilestore.exception.common;

import khanhtnd.mobilestore.exception.CustomException;
import khanhtnd.mobilestore.utils.Message;
import lombok.Getter;

@Getter
public class InvalidImageException extends CustomException {
    private final Message description;

    public InvalidImageException(Message description) {
        super(description);
        this.description = description;
    }
}