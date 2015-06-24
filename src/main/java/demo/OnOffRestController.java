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

    @RequestMapping(value = "/sleep", params = "max")
    public void sleepRandom(
            @RequestParam(value = "min", defaultValue = "1") final int minDuration,
            @RequestParam(value = "max", defaultValue = "45") final int maxDuration,
            @RequestParam(value = "unit", defaultValue = "SECONDS") final TimeUnit unit) {

        final int duration = minDuration + (int)(Math.random() * ((maxDuration - minDuration) + 1));

        doWait(unit, duration);
    }

    @RequestMapping("/sleep")
    public void sleep(@RequestParam(value = "duration") final int duration,
                      @RequestParam(value = "unit", defaultValue = "SECONDS") final TimeUnit unit) {
        doWait(unit, duration);
    }

    private void doWait(TimeUnit unit, int duration) {
        System.out.println("Waiting " + duration + " " + unit);
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {
        }
    }

    @RequestMapping(value = "/sleep", params = {"!duration", "!max"})
    public String sleep() {

        final StringBuffer units = new StringBuffer();
        for (final TimeUnit unit : TimeUnit.values()) {
            units.append("<br/>\n - ").append(unit.name());
        }

        return "Usage 1: fixed-length wait<br/>\n"
                + "/sleep?duration=3&unit=SECONDS<br/>\n"
                + "<br/>\n"
                + "<br/>\n"
                + "Usage 2: random wait between an interval<br/>\n"
                + "/sleep?min=3&max=60&unit=SECONDS<br/>\n"
                + "<br/>\n"
                + "<br/>\n"
                + "where unit can be one of:<br/>"
                + units.toString()
                + "<br/>\n"
                + "<br/>\n"
                + "When unit is not specified, SECONDS is used<br/>";

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
