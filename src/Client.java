import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client implements IClient {

	private static InetAddress MY_IP;

	private static final int SEARCH_RATE = 3000;

	static {
		try {
			MY_IP = InetAddress.getLocalHost();
		} catch (Exception e) {
		}
	}

	private Sender s;
	private Receiver r;

	private DatagramSocket ds;

	private ServerSearcher searcher;

	public Client() {
		this.searcher = new ServerSearcher(MY_IP.getHostAddress(), this, new Ports(), SEARCH_RATE);
	}

	@Override
	public void connectToServer(String serverAddress, int clientServerPort, int serverClientPort) {
		this.s = new Sender(serverAddress, clientServerPort, "COUCOU JE SUIS UN MESSAGE CLIENT");
		this.r = new Receiver(serverAddress, serverClientPort);
		this.start();
	}

	public void setSearcherEnable(boolean state) {
		this.searcher.setSearchingServer(state);
	}

	private void start() {
		this.s.start();
		this.r.start();
	}
}
