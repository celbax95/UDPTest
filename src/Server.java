import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server implements IServer {

	private static InetAddress MY_IP;

	static {
		try {
			MY_IP = InetAddress.getLocalHost();
		} catch (Exception e) {
		}
	}

	private DatagramSocket ds;

	private List<Sender> senders;
	private List<Receiver> receivers;

	private List<String> clients;

	private ServerLinker linker;

	public Server() {
		this.senders = new ArrayList<>();
		this.receivers = new ArrayList<>();

		this.clients = new ArrayList<>();

		this.linker = new ServerLinker(MY_IP.getHostAddress(), this, new Ports());
	}

	public void setLinkerEnable(boolean state) {
		this.linker.setListeningForNew(state);
	}

	@Override
	public void someoneConnect(String clientIp, int clientServerPort) {
		if (this.clients.contains(clientIp))
			return;

		this.clients.add(clientIp);

		Receiver r = new Receiver(clientIp, clientServerPort);
		r.start();
		this.receivers.add(r);

		Sender s = new Sender(clientIp, new Ports().SERVER_CLIENT, "Je suis un message serveur pour l'ip " + clientIp);
		s.start();
		this.senders.add(s);
	}
}
