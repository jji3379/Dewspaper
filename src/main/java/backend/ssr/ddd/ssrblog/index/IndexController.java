package backend.ssr.ddd.ssrblog.index;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class IndexController {

    @GetMapping("/")
    public String main() throws Exception{
        return "hello world!";
    }
}
