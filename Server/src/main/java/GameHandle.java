import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakiir on 14/11/16.
 */

public class                GameHandle implements Runnable {
    protected List<String>  messages;
    protected boolean       gameRunning;

    public                  GameHandle() {
        this.messages = new ArrayList<String>();
        this.gameRunning = true;
    }

    public void             addMessage(String message) {
        this.messages.add(message);
    }

    public void             removeMessage(String message) {
        this.messages.remove(message);
    }

    public void             stopGameBrutally() {
        this.messages.clear();
        this.gameRunning = false;
        // do other things
    }

    @Override
    public void             run() {
        while (this.gameRunning) {
            try {
                System.out.println("[>] Reading Queue ...");
                if (this.messages.size() > 0) {
                    String lastMessage = this.messages.get(this.messages.size() - 1);
                    System.out.println("[>] Game Handle Message : " + lastMessage.replace('\n', '\0'));
                    this.removeMessage(lastMessage);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[+] Finishing Thread ..");
    }
}
