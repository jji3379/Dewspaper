package backend.ssr.ddd.ssrblog.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@Controller
@ApiIgnore
public class IndexController {

    @GetMapping("/")
    public String main() throws Exception{
        return "index/index";
    }

}
