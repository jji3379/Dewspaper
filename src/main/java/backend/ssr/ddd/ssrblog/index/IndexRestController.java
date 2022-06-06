package backend.ssr.ddd.ssrblog.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexRestController {

    @GetMapping("/")
    public String main() throws Exception{
        return "test";
    }

}
