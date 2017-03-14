package taskLightSOEPA;

import java.io.IOException;
import java.util.ArrayList;

public class TaskLightNotification implements Runnable {
	private ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList;
	private TaskLight taskLight;

	public TaskLightNotification(ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList, TaskLight taskLight) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.TaskLightDimmingStatusNotificationList = TaskLightDimmingStatusNotificationList;
		this.taskLight = taskLight;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList<Integer> tmpInt = new ArrayList<Integer>();
		for (int i = 0; i < TaskLightDimmingStatusNotificationList.size(); i++) {
			if (!TaskLightDimmingStatusNotificationList.get(i).notification(taskLight.getDimmingValue(), taskLight.getToningValue())) {
				tmpInt.add(i);
			}
		}

		for (int i = 0; i < tmpInt.size(); i++) {
			try {
				TaskLightDimmingStatusNotificationList.get(tmpInt.get(i) - i).close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				// e.printStackTrace();
			}
			TaskLightDimmingStatusNotificationList.remove(tmpInt.get(i) - i);
		}
	}
}