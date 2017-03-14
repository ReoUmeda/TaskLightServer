package taskLightSOEPA;

public class TaskLightDesk {
	private static final int maximumNumberOfRegisteredPersons = 4;
	private DimmingInformation[] informationOnOccupants = new DimmingInformation[maximumNumberOfRegisteredPersons];
	private final static String LOCK = "";

	public boolean presenceConfirmation(String name) {
		boolean ret = false;

		for (DimmingInformation str : informationOnOccupants) {
			if (name.equals(str.getUuid())) {
				ret = true;
			}
		}

		return ret;
	}

	/**
	 * すでに在席していてもtrue
	 *
	 * @param informationOnOccupants
	 * @return
	 */
	public boolean toBePresent(DimmingInformation informationOnOccupants) {
		boolean ret = false;
		for (int i = 0; i < numberOfPeopleInAttendance(); i++) {
			if (informationOnOccupants.getUuid().equals(this.informationOnOccupants[i].getUuid())) {
				ret = true;
				synchronized (LOCK) {
					this.informationOnOccupants[i] = informationOnOccupants;
				}
				break;
			}
		}

		// 在席している人が再調光用
		if (!ret) {
			for (int i = 0; i < this.informationOnOccupants.length; i++) {
				if (this.informationOnOccupants[i] == null) {
					ret = true;
					synchronized (LOCK) {
						this.informationOnOccupants[i] = informationOnOccupants;
					}

					break;
				}
			}

		}

		return ret;
	}

	public boolean toLeave(String name) {
		boolean ret = false;

		for (int i = 0; i < informationOnOccupants.length; i++) {
			if (informationOnOccupants[i] != null && name.equals(informationOnOccupants[i].getUuid())) {
				ret = true;
				synchronized (LOCK) {
					informationOnOccupants[i] = null;
				}
				break;
			}
		}

		return ret;
	}

	public int numberOfPeopleInAttendance() {
		int ret = 0;
		for (DimmingInformation str : informationOnOccupants) {
			if (str != null) {
				ret++;
			}
		}

		return ret;
	}

	public String[] nameOfThePersonPresent() {
		String[] ret = new String[informationOnOccupants.length];
		for (int i = 0; i < informationOnOccupants.length; i++) {
			if (informationOnOccupants[i] == null) {
				ret[i] = null;
			} else {
				ret[i] = informationOnOccupants[i].getUuid();
			}

		}

		return ret;
	}

	public int averageDimmingValue() {
		int ret = -1;

		int tmpdv = 0;

		for (DimmingInformation i : informationOnOccupants) {
			if (i != null) {
				tmpdv += i.getDimmingValue();
			}
		}

		if (numberOfPeopleInAttendance() > 0) {
			tmpdv = tmpdv / numberOfPeopleInAttendance();
		}

		ret = tmpdv;

		return ret;
	}

	public int averageToningValue() {
		int ret = -1;

		int tmptv = 0;

		for (DimmingInformation i : informationOnOccupants) {
			if (i != null) {
				tmptv += i.getToningValue();
			}
		}
		if (numberOfPeopleInAttendance() > 0) {
			tmptv = tmptv / numberOfPeopleInAttendance();
		}

		ret = tmptv;

		return ret;
	}

}
