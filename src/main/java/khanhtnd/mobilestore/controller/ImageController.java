package khanhtnd.mobilestore.controller;
import khanhtnd.mobilestore.model.Image;
import khanhtnd.mobilestore.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/images")
public class ImageController {

    private final CommonService<Image> commonService;

    @Autowired
    public ImageController(CommonService<Image> commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id) {
        Image image = commonService.getById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(image.getData());
    }
}