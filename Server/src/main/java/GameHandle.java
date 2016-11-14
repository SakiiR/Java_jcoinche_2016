import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakiir on 14/11/16.
 */

public class                GameHandle implements Runnable {
    protected List<String>  messages;

    public                  GameHandle() {
        this.messages = new ArrayList<String>();
    }

    public void             addMessage(String message) {
        this.messages.add(message);
    }

    public void             removeMessage(String message) {
        this.messages.remove(message);
    }

    @Override
    public void             run() {
        while (true) {
            try {
                System.out.println("[>] Reading Queue ...");
                if (this.messages.size() > 0) {
                    String lastMessage = this.messages.get(this.messages.size() - 1);
                    System.out.println(lastMessage);
                    this.removeMessage(lastMessage);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
