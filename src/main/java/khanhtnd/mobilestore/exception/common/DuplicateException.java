package khanhtnd.mobilestore.exception.common;

import khanhtnd.mobilestore.exception.CustomException;
import khanhtnd.mobilestore.utils.Message;
import lombok.Getter;


@Getter
public class DuplicateException extends CustomException {
    private final String obj;

    public DuplicateException(Message description, String obj) {
        super(description);
        this.obj = obj;
    }
}