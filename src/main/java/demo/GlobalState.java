package demo;

import org.springframework.stereotype.Service;

@Service
public class GlobalState {

    private boolean state = true;

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
