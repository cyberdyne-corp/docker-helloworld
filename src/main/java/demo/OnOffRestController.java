package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnOffRestController {

    @Autowired
    private GlobalState globalState;

    @RequestMapping("/state")
    public Boolean current() {
        return globalState.getState();
    }

    @RequestMapping("/on")
    public Boolean enable() {
        globalState.setState(true);
        return globalState.getState();
    }

    @RequestMapping("/off")
    public Boolean disable() {
        globalState.setState(false);
        return globalState.getState();
    }


}
