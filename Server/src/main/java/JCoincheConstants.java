/**
 * Created by sakiir on 14/11/16.
 */

/**
 * This class is only contains static attributes.
 * They are basic const information and messages to display
 * relative to the Server Module.
 *
 * @see GameThread
 */
public class                                JCoincheConstants {
    public static final String              project_name = "JCoincheServer";
    public static final String              project_usage = "[!] Usage : java -jar target/jcoinche-server HOST PORT";
    public static final String              log_game_thread_status = "[>] Game Thread .. with %d clients .. Reading Queue ..";
    public static final String              log_message_found = "[>] Found Message : %s";
    public static final String              log_using_default_port = "[>] No Arguments .. Using Default Port [1337]";
    public static final String              log_failed_parse_int = "[-] Failed to parse int [%s]";
    public static final String              log_game_started = "[>] Game Started !";
    public static final String              log_game_stopped = "[>] Game Stopped !";
    public static final String              log_server_starting = "[>] Starting Server ..";
    public static final String              log_server_started = "[>] Server Listenning on port %d";
    public static final String              log_failed_bind = "[-] Failed to bind port %d";
    public static final String              log_client_count = "[>] Connected Clients %d";
    public static final String              log_client_disconnected = "[>] Client Disconnected";
    public static final String              log_game_process_starting = "[>] Starting Game Process ! (:";
    public static final String              log_sending_data_to_game_process = "[>] Sending [%s] To Game Process !";
    public static final String              log_generating_cards = "[>] Generating Cards ..";
    public static final String              log_generated_cards = "[>] Cards Generated !";
    public static final String              log_spreading_cards = "[>] Spreading Cards ..";
    public static final String              log_spreaded_cards = "[>] Cards Spreaded !";
    public static final String              log_card_info = "[>] {Color : %s, Id : %s}";
    public static final String              log_each_player_card_infos = "[>] Player[%d]'s  Cards :";
}
