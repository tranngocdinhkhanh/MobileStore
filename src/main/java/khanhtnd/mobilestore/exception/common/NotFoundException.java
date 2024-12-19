package khanhtnd.mobilestore.exception.common;



import khanhtnd.mobilestore.exception.CustomException;
import khanhtnd.mobilestore.utils.Message;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {
    private final int id;

    public NotFoundException(Message description, int id) {
        super(description);
        this.id = id;
    }
}
