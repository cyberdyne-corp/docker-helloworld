package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class OnOffRestController {

    @Autowired
    private GlobalState globalState;

    @RequestMapping("/state")
    public Boolean current() {
        return globalState.getState();
    }

    @RequestMapping("/sleep")
    public void sleep(@RequestParam(value = "duration") final int duration,
                      @RequestParam(value = "unit", defaultValue = "SECONDS") final TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {
        }
    }

    @RequestMapping(value = "/sleep", params = "!duration")
    public String sleep() {
        final StringBuffer units = new StringBuffer();
        for (final TimeUnit unit : TimeUnit.values()) {
            units.append("\n - ").append(unit.name());
        }

        return "Usage: /sleep?duration=3&unit=SECONDS\n"
                + "\n"
                + "where unit can be one of:"
                + units.toString()
                + "\n"
                + "\n"
                + "When unit is not specified, SECONDS is used";
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
