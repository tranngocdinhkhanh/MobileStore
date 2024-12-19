package khanhtnd.mobilestore.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<T> data;

}
