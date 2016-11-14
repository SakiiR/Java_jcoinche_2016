import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakiir on 14/11/16.
 */
public class                    GameThread implements Runnable {

    private boolean             isRunning = true;
    private ChannelGroup        channelGroup = null;
    private List<String>        messages;

    public                      GameThread(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
        this.messages = new ArrayList<String>();
    }

    public void                 sendToAllChannel(String message) {
        for (Channel ch : this.channelGroup) {
            ch.writeAndFlush(message);
        }
    }

    @Override
    public void                 run() {
        // Game Start
        this.sendToAllChannel("Game is Starting !\r\n");
        while (this.isRunning) {
            try {
                System.out.println("[>] Game Thread .. with " + this.channelGroup.size() + " clients .. Reading Queue ..");
                if (this.messages.size() > 0) {
                    String lastMessage = this.messages.get(this.messages.size() - 1);
                    System.out.println("[>] Found Message : " + lastMessage);
                    this.removeMessage(lastMessage);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void                 stopGame() {
        this.isRunning = false;
        this.messages.clear();
        this.sendToAllChannel("[+] Game stopped !\r\nYou are in the waiting queue ..");
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
