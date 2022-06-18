package backend.ssr.ddd.ssrblog.index;

import backend.ssr.ddd.ssrblog.common.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class IndexRestController {

    private final S3Uploader s3Uploader;


    @PostMapping("/upload")
    public String uploadtest(@RequestParam("uploadfile") MultipartFile multipartFile) throws Exception{
        try {
            s3Uploader.uploadFiles(multipartFile, "static");
        } catch (Exception e) { return "fail"; }

        return "main";
    }

}
