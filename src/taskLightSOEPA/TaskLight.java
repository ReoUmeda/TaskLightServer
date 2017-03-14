package taskLightSOEPA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TaskLight {
	private int dimmingValue = -1;
	private int toningValue = -1;

	public synchronized boolean dimmingMix(int dv, int tv) {
		boolean ret = true;

		File file = new File("");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

			pw.println(fromlxToDimmingValue(dv) + "," + fromKToToningValue(tv));

			pw.close();
			dimmingValue = dv;
			toningValue = tv;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public boolean reDimmingMix() {
		return dimmingMix(dimmingValue, toningValue);
	}

	public int getDimmingValue() {
		return dimmingValue;
	}

	public int getToningValue() {
		return toningValue;
	}

	private int fromlxToDimmingValue(int dv) {
		int ret = -1;

		// Aさん(100,0),(600lx,3000K)
		// Bさん(255,255),(1200lx,4600K)
		// A+B(175,165),(900lx,3800K)

		if (dv == 600) {
			ret = 100;
		} else if (dv == 900) {
			ret = 175;
		} else if (dv == 1200) {
			ret = 255;
		}// 適当あとで削除
		else {
			ret = (int) (0.2583 * (double) dv - 55.833);
			if (ret < 0) {
				ret = 0;
			} else if (ret > 255) {
				ret = 255;
			}
		}

		return ret;
	}

	private int fromKToToningValue(int tv) {
		int ret = -1;

		// Aさん(100,0),(600lx,3000K)
		// Bさん(255,255),(1200lx,4600K)
		// A+B(175,165),(900lx,3800K)

		if (tv == 3000) {
			ret = 0;
		} else if (tv == 3800) {
			ret = 165;
		} else if (tv == 4600) {
			ret = 255;
		}// 適当あとで削除
		else {
			ret = (int) (-6E-05 * (double) tv * (double) tv + 0.6047
					* (double) tv - 1286.7);
			if (ret < 0) {
				ret = 0;
			} else if (ret > 255) {
				ret = 255;
			} else if (4600 < tv) {
				ret = 255;
			}

		}

		return ret;
	}

}
