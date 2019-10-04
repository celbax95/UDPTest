import java.util.Scanner;

public class Main {

	private static String IP = "127.0.0.1";

	public static void client() {
		Client c = new Client();
		c.setSearcherEnable(true);

		Scanner sc = new Scanner(System.in);

		System.out.println("Pour arreter le searcher tapez n'import quoi, puis entrer\n");

		sc.nextLine();

		c.setSearcherEnable(false);
	}

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		System.out.println("1 - Serveur\n2 - Client\n\n -- : ");

		int ind = sc.nextInt();

		if (ind == 1) {
			server();
		} else {
			client();
		}
	}

	public static void server() {
		Server s = new Server();
		s.setLinkerEnable(true);

		Scanner sc = new Scanner(System.in);

		System.out.println("Pour arreter le linker tapez n'import quoi, puis entrer\n");

		sc.nextLine();

		s.setLinkerEnable(false);
	}
}
