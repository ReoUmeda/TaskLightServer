package taskLightSOEPA;

import java.util.ArrayList;

public class SOEPATaskLightStart {
	public static final boolean testFlag = true;

	public static void main(String[] args) {
		int[] port = new int[3];// サーバーのポート0番目がDimming用1番目がサーバーからPUSH用

		int count = 0;
		try {
			for (String str : args) {
				port[count] = Integer.parseInt(str);
				count++;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("引数が間違っています");
			System.out.println("引数は整数のみです");
			System.exit(0);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("引数の数が間違っています");
			System.out.println("引数は3つまでです");
			System.exit(0);
		}

		TaskLightServer tlsDimming = null;
		TaskLightServer tlsTaskLightServerDimmingState = null;
		TaskLightServer tlsTasklightServerLeaveSeat = null;

		ArrayList<TaskLightDimmingStatusNotification> alt = new ArrayList<TaskLightDimmingStatusNotification>();
		TaskLightDesk taskLightDesk = new TaskLightDesk();
		TaskLight taskLight = new TaskLight();

		if (count == 3) {
			tlsDimming = (TaskLightServer) new TaskLightServerDimming(port[0], alt, taskLightDesk, taskLight);
			tlsTaskLightServerDimmingState = (TaskLightServer) new TaskLightServerDimmingState(port[1], alt);
			tlsTasklightServerLeaveSeat = (TaskLightServer) new TasklightServerLeaveSeat(port[2], taskLightDesk, alt, taskLight);
		} else if (count == 2) {
			tlsDimming = (TaskLightServer) new TaskLightServerDimming(port[0], alt, taskLightDesk, taskLight);
			tlsTaskLightServerDimmingState = (TaskLightServer) new TaskLightServerDimmingState(port[1], alt);
			tlsTasklightServerLeaveSeat = (TaskLightServer) new TasklightServerLeaveSeat(taskLightDesk, alt, taskLight);
		} else if (count == 1) {
			tlsDimming = (TaskLightServer) new TaskLightServerDimming(port[0], alt, taskLightDesk, taskLight);
			tlsTaskLightServerDimmingState = (TaskLightServer) new TaskLightServerDimmingState(alt);
			tlsTasklightServerLeaveSeat = (TaskLightServer) new TasklightServerLeaveSeat(taskLightDesk, alt, taskLight);
		} else {
			tlsDimming = (TaskLightServer) new TaskLightServerDimming(alt, taskLightDesk, taskLight);
			tlsTaskLightServerDimmingState = (TaskLightServer) new TaskLightServerDimmingState(alt);
			tlsTasklightServerLeaveSeat = (TaskLightServer) new TasklightServerLeaveSeat(taskLightDesk, alt, taskLight);
		}

		Thread t1 = new Thread(tlsTaskLightServerDimmingState);
		t1.start();
		Thread t2 = new Thread(tlsTasklightServerLeaveSeat);
		t2.start();
		tlsDimming.start();

	}

}
