package taskLightSOEPA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class TaskLightDimming implements Runnable {
	private Socket dimmingSocket = null;
	private TaskLightDesk forPresence = null;
	private BufferedReader br;
	private ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList = null;
	private PrintWriter pw = null;
	private TaskLight taskLight = null;
	private boolean success = true;

	public TaskLightDimming(Socket sc, TaskLightDesk forPresence,
			ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList, TaskLight taskLight) {
		// TODO 自動生成されたコンストラクター・スタブ
		dimmingSocket = sc;
		this.forPresence = forPresence;
		this.TaskLightDimmingStatusNotificationList = TaskLightDimmingStatusNotificationList;
		this.taskLight = taskLight;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		try {
			br = new BufferedReader(new InputStreamReader(
					dimmingSocket.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					dimmingSocket.getOutputStream())));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// データリード用(DimmingInformationに変換する用)
		String[] tmp = new String[3];
		try {
			tmp[0] = br.readLine();
			tmp[1] = br.readLine();
			tmp[2] = br.readLine();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		DimmingInformation informationOnOccupants = new DimmingInformation(
				Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), tmp[2]);

		boolean presenceSuccess = forPresence.toBePresent(informationOnOccupants);

		if (presenceSuccess) {
			boolean dimmingSuccess = taskLight.dimmingMix(forPresence.averageDimmingValue(), forPresence.averageToningValue());
			if (dimmingSuccess) {
				Thread t1 = new Thread(new TaskLightNotification(TaskLightDimmingStatusNotificationList, taskLight));
				t1.start();
			}
		} else {
			success = false;
		}

		pw.println(success);
		pw.flush();

		try {
			pw.close();
			br.close();
			dimmingSocket.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
