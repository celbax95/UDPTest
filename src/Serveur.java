public class Serveur {

	private static String IP = "127.0.0.1";

	private static int PORTS = 5000;
	private static int PORTR = 5001;

	private Sender s;
	private Receiver r;

	public Serveur() {
		this.s = new Sender(IP, PORTS, "COUCOU JE SUIS UN MESSAGE SERVEUR");
		this.r = new Receiver(IP, PORTR);
		this.ready();
	}

	@SuppressWarnings("resource")
	private void ready() {
		this.s.start();
		this.r.start();
	}
}
