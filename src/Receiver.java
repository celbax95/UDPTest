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

		Cooldown c = new Cooldown(1000);
		c.start();

		int cpt = 0;

		try {
			while (true) {

				byte[] buffer = new byte[8192];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				this.ds.receive(packet);

				String msg = new String(packet.getData());

				cpt++;

				if (c.resetOnDone()) {
					System.out.println(cpt + "\t: msg / sec");
					System.out.println(Math.round((double) 1000 / cpt * 100.) / 100. + "\t: inter / msg");
					System.out.println("\n\n");
					cpt = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.myThread.start();
	}
}
