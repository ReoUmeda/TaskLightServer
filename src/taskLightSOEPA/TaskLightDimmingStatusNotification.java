package taskLightSOEPA;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TaskLightDimmingStatusNotification {
	private Socket clientNotificationSocket = null;
	private ObjectOutputStream oos;
	private String ip;

	public TaskLightDimmingStatusNotification(Socket s) {
		clientNotificationSocket = s;
		try {
			oos = new ObjectOutputStream(clientNotificationSocket.getOutputStream());
			ip = clientNotificationSocket.getInetAddress().getHostAddress();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public boolean notification(int dv, int tv) {
		boolean ret = true;

		try {
			oos.writeObject(new DimmingInformation(dv, tv, "Server"));
			oos.flush();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			// e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public void close() throws IOException {
		clientNotificationSocket.close();
		oos.close();
	}

	public String getIp() {
		return ip;
	}

}
