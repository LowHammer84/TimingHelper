import java.util.Timer;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    
    Window window;
    String message;

    
    public MyTimerTask(Window window, String message) {
        this.window = window;
        this.message = message;
    }
    
    @Override
    public void run() {
        //window.showDialog(message);
    }
}
