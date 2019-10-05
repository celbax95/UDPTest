import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class ServerSearcher {

	private final int serverInfosArrayAddress = 0, serverInfosArrayClientServerPort = 1,
			serverInfosArrayServerClientPort = 2;

	private String clientIp;

	private Ports ports;

	private Thread searcher, receiver;

	private boolean searchingServer;

	private IClient client;

	public ServerSearcher(String clientIp, IClient client, Ports ports) {
		super();
		this.clientIp = clientIp;
		this.client = client;
		this.ports = ports;

		this.searcher = null;
		this.receiver = null;

		this.searchingServer = false;
	}

	private boolean isAddress(String address) {
		try {
			InetAddress.getByName(address);
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
	}

	private boolean isPort(String port) {
		int tmport = -1;
		try {
			tmport = Integer.parseInt(port);
		} catch (Exception e) {
			return false;
		}

		return tmport >= 0 && tmport <= 65535;
	}

	public boolean isSearchingServer() {
		return this.searchingServer;
	}

	/**
	 * Recevoir l'adresse ip du serveur
	 */
	private void receiveServerInfos() {
		final MulticastSocket socket;
		final InetAddress group;
		try {
			socket = new MulticastSocket(this.ports.LINKER_SEARCHER);
			socket.setInterface(InetAddress.getLocalHost());
			group = InetAddress.getByName(IPs.MULTICAST_GROUP);
			socket.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		(this.receiver = new Thread(() -> {
			while (ServerSearcher.this.searchingServer && !socket.isClosed()) {

				byte[] buffer = new byte[8192];

				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				try {
					socket.receive(packet);
				} catch (IOException e1) {
					e1.printStackTrace();
					Thread.currentThread().interrupt();
					return;
				}

				String serverInfos = new String(packet.getData());

				ServerSearcher.this.tryToConnect(serverInfos);
			}
			try {
				socket.leaveGroup(group);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			socket.close();
		})).start();
	}

	private void searchServer() {
		(this.searcher = new Thread(() -> {
			while (ServerSearcher.this.searchingServer && !Thread.currentThread().isInterrupted()) {
				ServerSearcher.this.sendMulticast(ServerSearcher.this.clientIp);
			}
		})).start();
	}

	private void sendMulticast(String message) {
		final InetAddress group;
		try {
			group = InetAddress.getByName(IPs.MULTICAST_GROUP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}

		MulticastSocket socket;
		try {
			socket = new MulticastSocket();
			socket.setInterface(InetAddress.getLocalHost());
			socket.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		byte[] buffer = message.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, this.ports.SEARCHER_LINKER);

		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
			return;
		}

		try {
			socket.leaveGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}

	public void setSearchingServer(boolean searchingServer) {
		if (this.searchingServer != searchingServer) {
			this.searchingServer = searchingServer;

			if (searchingServer = false) {
				if (this.searcher != null) {
					this.searcher.interrupt();
				}
				if (this.receiver != null) {
					this.receiver.interrupt();
				}
			} else {
				this.receiveServerInfos();
				this.searchServer();
			}
		}
	}

	private void tryToConnect(String serverInfos) {

		String[] serverInfosArray = serverInfos.split("/");

		if (serverInfosArray.length != 4)
			return;

		if (ServerSearcher.this.isAddress(serverInfosArray[ServerSearcher.this.serverInfosArrayAddress])
				&& ServerSearcher.this.isPort(serverInfosArray[ServerSearcher.this.serverInfosArrayClientServerPort])
				&& ServerSearcher.this.isPort(serverInfosArray[ServerSearcher.this.serverInfosArrayServerClientPort])) {

			ServerSearcher.this.setSearchingServer(false);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			ServerSearcher.this.client.connectToServer(serverInfosArray[ServerSearcher.this.serverInfosArrayAddress],
					Integer.parseInt(serverInfosArray[ServerSearcher.this.serverInfosArrayClientServerPort]),
					Integer.parseInt(serverInfosArray[ServerSearcher.this.serverInfosArrayServerClientPort]));
		}
	}
}
