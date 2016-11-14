import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakiir on 14/11/16.
 */
public class                    GameThread implements Runnable {
    private boolean             isRunning = true;
    private ChannelGroup        channelGroup = null;
    private List<String>        messages = null;
    private GameHandle          gameHandle = null;

    public                      GameThread(ChannelGroup channelGroup, GameHandle gameHandle) {
        this.channelGroup = channelGroup;
        this.gameHandle = gameHandle;
        this.messages = new ArrayList<>();
    }

    @Override
    public void                 run() {
        this.gameHandle.sendToAllChannel("[>] Game is Starting !\r\n");
        while (this.isRunning) {
            try {
                System.out.println(String.format(JCoincheConstants.log_game_thread_status, this.channelGroup.size()));
                if (this.messages.size() > 0) {
                    String lastMessage = this.messages.get(this.messages.size() - 1);
                    System.out.println(String.format(JCoincheConstants.log_message_found, lastMessage));
                    this.removeMessage(lastMessage);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public GameThread           stopGame() {
        this.isRunning = false;
        this.messages.clear();
        this.gameHandle.sendToAllChannel("[>] Game stopped !\r\n[>]You are in the waiting queue ..\r\n");
        return this;
    }

    public GameThread           addMessage(String message) {
        this.messages.add(message);
        return this;
    }

    public GameThread           removeMessage(String message) {
        this.messages.remove(message);
        return this;
    }
}
