import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ServerLinker {

	private String serverIP;

	private Thread listener, sender;

	private Map<String, Integer> assignedPorts;

	private boolean listeningForNew;

	private Ports ports;

	private IServer server;

	private int clientServerPort;

	public ServerLinker(String serverIP, IServer server, Ports ports) {
		super();
		this.serverIP = serverIP;

		this.server = server;

		this.ports = ports;

		this.clientServerPort = ports.CLIENT_SERVER_START;

		this.assignedPorts = new HashMap<>();

		this.listener = null;
		this.sender = null;

		this.listeningForNew = false;
	}

	private boolean isAddress(String address) {
		try {
			InetAddress.getByName(address);
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
	}

	@SuppressWarnings("unused")
	private boolean isListeningForNew() {
		return this.listeningForNew;
	}

	private void linkClient(String ipClient) {
		if (!this.isAddress(ipClient))
			return;

		if (!this.assignedPorts.containsKey(ipClient)) {
			this.assignedPorts.put(ipClient, this.clientServerPort++);
		}

		this.sendServerInfos(ipClient);

	}

	private void listenRequests() {
		final MulticastSocket socket;
		final InetAddress group;

		try {
			socket = new MulticastSocket(this.ports.SEARCHER_LINKER);
			socket.setInterface(InetAddress.getLocalHost());
			group = InetAddress.getByName(IPs.MULTICAST_GROUP);
			socket.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		(this.listener = new Thread(() -> {
			byte[] buffer = new byte[8192];

			while (ServerLinker.this.listeningForNew && !ServerLinker.this.listener.isInterrupted()
					&& !socket.isClosed()) {

				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				try {
					socket.receive(packet);
				} catch (IOException e1) {
					e1.printStackTrace();
					Thread.currentThread().interrupt();
					return;
				}

				String ipClient = new String(packet.getData());

				ServerLinker.this.linkClient(ipClient);
			}
			try {
				socket.leaveGroup(group);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			socket.close();
		})).start();
	}

	private void sendServerInfos(String address) {
		if (!this.isAddress(address) || !this.assignedPorts.containsKey(address))
			return;

		final InetAddress group;
		try {
			group = InetAddress.getByName(IPs.MULTICAST_GROUP);
		} catch (UnknownHostException e) {
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

		(this.sender = new Thread(() -> {

			byte[] buffer = (ServerLinker.this.serverIP + "/" + ServerLinker.this.assignedPorts.get(address) + "/"
					+ ServerLinker.this.ports.SERVER_CLIENT + "/").getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group,
					ServerLinker.this.ports.LINKER_SEARCHER);

			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			ServerLinker.this.server.someoneConnect(address, ServerLinker.this.assignedPorts.get(address));

			try {
				socket.leaveGroup(group);
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket.close();
		})).start();
	}

	public void setListeningForNew(boolean listeningForNew) {
		if (this.listeningForNew != listeningForNew) {
			this.listeningForNew = listeningForNew;

			if (this.listeningForNew == false) {
				if (this.listener != null) {
					this.listener.interrupt();
				}
				if (this.sender != null) {
					this.sender.interrupt();
				}
			} else {
				this.listenRequests();
			}
		}
	}
}
