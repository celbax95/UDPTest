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

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.myThread = new Thread(this);
		this.port = port;
	}

	@Override
	public void run() {

		int cpt = 0, max = 5000;

		try {
			while (true) {
				Thread.sleep(1);

				byte[] buffer = new byte[8192];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				this.ds.receive(packet);

				String msg = new String(packet.getData());

				cpt %= Integer.MAX_VALUE;
				System.out.println(cpt++ + " - " + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.myThread.start();
	}
}
