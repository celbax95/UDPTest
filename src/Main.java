import java.util.Scanner;

public class Main {

	private static String IP = "127.0.0.1";

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		System.out.println("1 - Serveur\n2 - Client\n\n -- : ");

		int ind = sc.nextInt();

		if (ind == 1)
			new Serveur();
		else
			new Client();
	}
}
