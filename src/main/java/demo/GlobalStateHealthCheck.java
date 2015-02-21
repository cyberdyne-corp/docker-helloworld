package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GlobalStateHealthCheck implements HealthIndicator {

    @Autowired
    private GlobalState globalState;

    @Override
    public Health health() {
        if (globalState.getState()) {
            return Health.up().build();
        }
        return Health.down().build();
    }

}
