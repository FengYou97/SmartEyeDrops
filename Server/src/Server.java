import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Server {
	public static void main(String[] args) throws Exception{
		try(var listener = new ServerSocket(8800)){
			System.out.println("Server is running");
			var pool = Executors.newFixedThreadPool(20);
			while(true) {
				pool.execute(new Test(listener.accept()));
			}
		}
	}
	
	private static class Test implements Runnable{

		private Socket socket;
		
		Test(Socket socket){
			this.socket = socket;
		}
		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				var in = new Scanner(socket.getInputStream());
				FileWriter wr = new FileWriter(new File("test.csv"));
				while(in.hasNextLine()) {
					String[] arr = in.nextLine().split(",");
					wr.append(String.join(",", arr));
					wr.append("\n");
				}
				wr.flush();
				wr.close();
				in.close();
				socket.close();
			}catch(Exception e) {
				System.out.println(e);
			}finally {
				System.out.println("Closed: " + socket);
				threshHold();
			}
		}
		
		private void threshHold() {
			try {
				Process p = Runtime.getRuntime().exec("python test1.py");
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				System.out.println(in.readLine());
				System.out.println(in.readLine());
				System.out.println(in.readLine());
				System.out.println(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
