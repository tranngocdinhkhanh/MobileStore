package khanhtnd.mobilestore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    MSG_101(101, "The provided image is invalid or cannot be processed."),
    MSG_102(102, "Image exceeds the maximum allowed size of 5 MB."),
    MSG_103(103, "Not find with id ."),
    MSG_201(201, "Create success."),
    MSG_202(202, "Get object success."),
    MSG_400(400, "Unexpected exception"),
    MSG_404(404, "Not found");
    private final int code;
    private final String description;


}
