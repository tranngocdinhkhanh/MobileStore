package khanhtnd.mobilestore.service;

import khanhtnd.mobilestore.dto.response.PageDto;
import org.springframework.data.domain.Pageable;

public interface CommonService<R> {
    R getById(int id);

    PageDto<R> getAll(Pageable pageable, String search);
}
