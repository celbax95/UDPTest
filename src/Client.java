public class Client {

	private static String IP = "127.0.0.1";

	private static int PORTS = 5001;
	private static int PORTR = 5000;

	private Sender s;
	private Receiver r;

	public Client() {
		this.s = new Sender(IP, PORTS, "COUCOU JE SUIS UN MESSAGE CLIENT");
		this.r = new Receiver(IP, PORTR);
		this.ready();
	}

	@SuppressWarnings("resource")
	private void ready() {
		this.s.start();
		this.r.start();
	}
}
