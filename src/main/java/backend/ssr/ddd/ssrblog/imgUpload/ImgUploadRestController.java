package backend.ssr.ddd.ssrblog.imgUpload;

import backend.ssr.ddd.ssrblog.common.S3Uploader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Api(tags = "5. 이미지 업로드 API")
public class ImgUploadRestController {

    private final S3Uploader s3Uploader;

    /**
     포스트 이미지 업로드
     */
    @PostMapping("/img/post")
    @ApiOperation(value = "포스트 이미지 업로드", notes = "포스트 글 작성중 이미지 업로드에 관한 api입니다. name은 postImg로 지정")
    public String imgPost(@RequestParam("postImg") MultipartFile multipartFile) throws Exception{

        return s3Uploader.uploadFiles(multipartFile, "post");

    }

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/img/profile")
    @ApiOperation(value = "프로필 이미지 업로드", notes = "프로필 이미지 업로드에 관한 api입니다. name은 profileImg로 지정")
    public String imgProfile(@RequestParam("profileImg") MultipartFile multipartFile) throws Exception{

        return s3Uploader.uploadFiles(multipartFile, "profile");

    }

    /**
     * 썸네일 이미지 업로드
     */
    @PostMapping("/img/thumbnail")
    @ApiOperation(value = "썸네일 이미지 업로드", notes = "썸네일 이미지 업로드에 관한 api입니다. name은 thumbnailImg로 지정")
    public String imgThumbnail(@RequestParam("thumbnailImg") MultipartFile multipartFile) throws Exception{

        return s3Uploader.uploadFiles(multipartFile, "img");

    }
}
