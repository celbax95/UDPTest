import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender implements Runnable {

	private InetAddress ip;
	private int port;
	private String msg;

	private DatagramSocket ds;

	private Thread myThread;

	public Sender(String ip, int port, String msg) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.ds = new DatagramSocket();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		this.myThread = new Thread(this);
		this.port = port;
		this.msg = msg;
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte[] buffer = this.msg.getBytes();
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.ip, this.port);

				this.ds.send(packet);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.myThread.start();
	}

}
