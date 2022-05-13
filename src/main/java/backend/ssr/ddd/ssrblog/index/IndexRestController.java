package backend.ssr.ddd.ssrblog.index;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexRestController {

    public String main() throws Exception{
        return "test";
    }

}
