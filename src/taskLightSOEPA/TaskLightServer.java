package taskLightSOEPA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class TaskLightServer implements Runnable {
	private int port;
	private ServerSocket serverSocket = null;
	private boolean isAccept = true;

	public TaskLightServer(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public abstract void start();

	public void stop() {
		isAccept = false;
	}

	public void serverSocketClose() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		start();
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public boolean isAccept() {
		return isAccept;
	}

	public void setPort(int port) {
		this.port = port;
	}

}

class TaskLightServerDimming extends TaskLightServer {
	private final static int port = 54931;
	private final int maximumNumberOfThreads = 255;
	private ExecutorService ex = null;
	private ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList = null;
	private TaskLightDesk forPresence = null;
	private TaskLight taskLight = null;

	public TaskLightServerDimming(ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification, TaskLightDesk TaskLightDesk,
			TaskLight taskLight) {
		super(port);
		TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
		forPresence = TaskLightDesk;
		this.taskLight = taskLight;
		setExecutorService();
	}

	public TaskLightServerDimming(int port, ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification, TaskLightDesk TaskLightDesk,
			TaskLight taskLight) {
		super(port);
		TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
		forPresence = TaskLightDesk;
		this.taskLight = taskLight;
		setExecutorService();
	}

	private void setExecutorService() {
		ExecutorService ret = null;

		ret = Executors.newFixedThreadPool(maximumNumberOfThreads, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});

		ex = ret;
	}

	@Override
	public void start() {
		while (isAccept()) {
			try {
				Socket sc = getServerSocket().accept();

				ex.submit(new TaskLightDimming(sc, forPresence, TaskLightDimmingStatusNotificationList, taskLight));

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}

class TaskLightServerDimmingState extends TaskLightServer {
	private final static int port = 54932;
	private ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList = null;

	public TaskLightServerDimmingState(ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification) {
		super(port);
		TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
	}

	public TaskLightServerDimmingState(int port, ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification) {
		super(port);
		TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
	}

	@Override
	public void start() {
		while (isAccept()) {
			try {
				Socket sc = getServerSocket().accept();

				TaskLightDimmingStatusNotification dimmerStatusNotification = new TaskLightDimmingStatusNotification(sc);

				TaskLightDimmingStatusNotificationList.add(dimmerStatusNotification);

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}

	public ArrayList<TaskLightDimmingStatusNotification> getTaskLightDimmingStatusNotificationList() {
		return TaskLightDimmingStatusNotificationList;
	}

}

class TasklightServerLeaveSeat extends TaskLightServer {
	private final static int port = 54933;
	private final int maximumNumberOfThreads = 255;
	private ExecutorService ex = null;
	private TaskLightDesk forAbsence = null;
	private ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList = null;
	private TaskLight taskLight = null;

	public TasklightServerLeaveSeat(TaskLightDesk TaskLightDesk, ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification,
			TaskLight taskLight) {
		super(port);
		setExecutorService();
		forAbsence = TaskLightDesk;
		TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
		this.taskLight = taskLight;
	}

	public TasklightServerLeaveSeat(int port, TaskLightDesk TaskLightDesk, ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification,
			TaskLight taskLight) {
		super(port);
		setExecutorService();
		forAbsence = TaskLightDesk;
		TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
		this.taskLight = taskLight;
	}

	@Override
	public void start() {
		while (isAccept()) {
			try {
				Socket sc = getServerSocket().accept();

				ex.submit(new ToGoAway(sc, forAbsence, TaskLightDimmingStatusNotificationList, taskLight));

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}
	}

	private void setExecutorService() {
		ExecutorService ret = null;

		ret = Executors.newFixedThreadPool(maximumNumberOfThreads, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});

		ex = ret;
	}

	class ToGoAway implements Runnable {
		private Socket socketForAbsence = null;
		private TaskLightDesk forAbsence = null;
		private BufferedReader br = null;
		private PrintWriter pw = null;
		private boolean success = true;
		private ArrayList<TaskLightDimmingStatusNotification> TaskLightDimmingStatusNotificationList = null;
		private TaskLight taskLight = null;

		public ToGoAway(Socket s, TaskLightDesk TaskLightDesk, ArrayList<TaskLightDimmingStatusNotification> dimmerStatusNotification,
				TaskLight taskLight) {
			socketForAbsence = s;
			forAbsence = TaskLightDesk;
			TaskLightDimmingStatusNotificationList = dimmerStatusNotification;
			this.taskLight = taskLight;
		}

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ

			try {
				br = new BufferedReader(new InputStreamReader(
						socketForAbsence.getInputStream()));
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						socketForAbsence.getOutputStream())));

				String tmpStr = br.readLine();

				success = forAbsence.toLeave(tmpStr);

				if (success) {
					taskLight.dimmingMix(forAbsence.averageDimmingValue(), forAbsence.averageToningValue());
					Thread t1 = new Thread(new TaskLightNotification(TaskLightDimmingStatusNotificationList, taskLight));
					t1.start();
				}

				// String ip = socketForAbsence.getInetAddress().getHostAddress();
				// for(int i=0;i<TaskLightDimmingStatusNotificationList.size();i++){
				// if(ip.equals(TaskLightDimmingStatusNotificationList.get(i).getIp())){
				// TaskLightDimmingStatusNotificationList.remove(i);
				// break;
				// }
				// }

				pw.println(success);

				pw.flush();

			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			try {
				br.close();
				pw.close();
				socketForAbsence.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}

	}

}
