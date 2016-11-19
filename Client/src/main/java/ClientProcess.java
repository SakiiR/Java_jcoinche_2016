/**
 * Created by sakiir on 19/11/16.
 */
public class                                    ClientProcess implements Runnable {

    private JCoincheProtocol.JCoincheMessage    lastMessage = null;

    public          ClientProcess() {  }

    @Override
    public void                                 run() {

    }

    public ClientProcess                        setLastMessage(JCoincheProtocol.JCoincheMessage lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public JCoincheProtocol.JCoincheMessage     getLastMessage() {
        return this.lastMessage;
    }
}
