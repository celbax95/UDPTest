import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver implements Runnable {

	private InetAddress ip;
	private int port;

	private DatagramSocket ds;

	private Thread myThread;

	public Receiver(String ip, int port) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.ds = new DatagramSocket(port);
			this.myThread = new Thread(this);
		} catch (Exception e) {
		}
		this.port = port;
	}

	@Override
	public void run() {

		int cpt = 0, max = 5000;

		try {
			while (true) {
				Thread.sleep(10);

				byte[] buffer = new byte[8192];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				this.ds.receive(packet);

				String msg = new String(packet.getData());

				if (cpt++ % max == 0) {
					System.out.println(msg);
				}
			}
		} catch (Exception e) {
			System.out.println("erreur");
		}
	}

	public void start() {
		this.myThread.start();
	}
}
