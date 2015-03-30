import java.io.PrintWriter;
import java.util.Scanner;

public class CalendarMaker {
	private final static int PUBLIC_PRIVATE = 1;
	private final static int PRIORITY = 2;
	private final static int LOCATION = 3;
	private final static int SUMMARY = 4;
	private final static int START_TIME = 5;
	private final static int END_TIME = 6;
	private final static int SHOW_DATA = 7;
	private final static int MAKE_CALENDAR = 8;
	private final static int QUIT = 9;
	
	private boolean 				isPublic;
	private int 					priority;
	private String					location;
	private String					summary;

	private class DateData {
		public int month;
		public int day;
		public int year;
		public int hour;
		public int minute;
		public int second;

		public DateData() {
			month = 0;
			day = 0;
			year= 0;
			hour = 0;
			minute = 0;
			second = 0;
		}
		
		public String format() {
			String format = new String(String.format("%04d", year) +
					String.format("%02d", month) + String.format("%02d", day) +
					"T" + String.format("%02d", hour) + String.format("%02d", minute) +
					String.format("%02d", second));
			return format;
		}

		public void show() {
			System.out.println("Month/Day/Year: " + month + "/" + day + "/" + year);
			System.out.println("Hour:Minute:Second: " + hour + ":" + minute + ":" + second);
		}
		
		public boolean grab() {
			System.out.println("Enter Month (1-12):");
			int inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 1 || inputInt > 12) return false;
			month = inputInt;

			System.out.println("Enter Day (1-32):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 1 || inputInt > 32) return false;
			day = inputInt;

			System.out.println("Enter Year (0-9999):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 9999) return false;
			year = inputInt;

			System.out.println("Enter Hour (24-hr format):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 23) return false;
			hour = inputInt;
			
			System.out.println("Enter Minute (0-59):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 59) return false;
			minute = inputInt;

			System.out.println("Enter Second (0-59):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 59) return false;
			second = inputInt;

			return true;
		}
	}
	private DateData				startTime;
	private DateData				endTime;
	
	private Scanner					scanner;
	
	public static void main(String[] args) {
		CalendarMaker maker = new CalendarMaker();
		maker.start();
	}
	
	public CalendarMaker() {
		scanner = new Scanner(System.in);
		isPublic = false;
		priority = 1;
		location =  new String("");
		summary = new String("");
		startTime = new DateData();
		endTime = new DateData();
	}
	
	public void start() {
		int inputInt = 0;
		boolean test = true;
		while (inputInt != QUIT) {
			System.out.println("(1)Public/Private, (2)Priority, (3)Location, " +
					"(4)Summary, (5)Start, (6)End, (7)Show Data, (8)Make Calendar, (9)Quit");
			try {
				inputInt = Integer.parseInt(scanner.nextLine());
			} catch (Exception e) {
				inputInt = -1;
			}
			test = true;
			switch (inputInt) {
			case PUBLIC_PRIVATE:
				test = grabPublic();
				break;
			case PRIORITY:
				test = grabPriority();
				break;
			case LOCATION:
				grabLocation();
				break;
			case SUMMARY:
				grabSummary();
				break;
			case START_TIME:
				test = grabStart();
				break;
			case END_TIME:
				test = grabEnd();
				break;
			case SHOW_DATA:
				showCurrentData();
				break;
			case MAKE_CALENDAR:
				System.out.println("Enter a filename to save data");
				String fileName = scanner.nextLine();
				makeCalendar(fileName);
				break;
			case QUIT:
				System.out.println("Quitting Program");
				return;
			default:
				System.out.println("Invalid Command");
			}
			if (!test) {
				System.out.println("Invalid Data Entered");
			}
		}
	}
	
	public boolean grabPublic() {
		System.out.println("(1)Public or (2)Private?");
		int inputInt = Integer.parseInt(scanner.nextLine());
		if (inputInt != 1 && inputInt != 2) return false;
		if (inputInt == 1) {
			isPublic = true;
		} else {
			isPublic = false;
		}
		return true;
	}
	
	public boolean grabPriority() {
		System.out.println("Enter a Priority (1-9)");
		int inputInt = Integer.parseInt(scanner.nextLine());
		if (inputInt < 1 || inputInt > 9) return false;
		priority = inputInt;
		return true;
	}
	
	public void grabLocation() {
		System.out.println("Enter a Location");
		location = scanner.nextLine();
	}
	
	public void grabSummary() {
		System.out.println("Enter a Summary");
		summary = scanner.nextLine();
	}
	
	public boolean grabStart() {
		System.out.println("------StartTime------");
		return startTime.grab();
	}
	
	public boolean grabEnd() {
		System.out.println("-------EndTime-------");
		return endTime.grab();
	}
	
	public void showCurrentData() {
		System.out.println("Calendar Information:");
		showPublic();
		showPriority();
		showLocation();
		showSummary();
		showStartTime();
		showEndTime();
	}
	
	
	public void showPublic() {
		System.out.println("Public: " + Boolean.toString(isPublic));
	}
	
	public void showPriority() {
		System.out.println("Priority: " + priority);
	}
	
	public void showLocation() {
		System.out.println("Location: " + location);
	}
	
	public void showSummary() {
		System.out.println("Summary: " + summary);
	}
	
	public void showStartTime() {
		System.out.println("------StartTime------");
		startTime.show();
		System.out.println("---------------------");
	}
	
	public void showEndTime() {
		System.out.println("-------EndTime-------");
		endTime.show();
		System.out.println("---------------------");
	}
	
	public void makeCalendar(String fileName) {
		String inputString = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error with file name");
			return;
		}
		writer.println("BEGIN:VCALENDAR");
		writer.println("VERSION:2.0");
		writer.println("BEGIN:VEVENT");
		if (isPublic) {
			writer.println("CLASS:PUBLIC");
		} else {
			writer.println("CLASS:PRIVATE");
		}
		writer.println("PRIORITY:" + priority);
		writer.println("LOCATION:" + location);
		writer.println("SUMMARY:" + summary);
		writer.println("DTSTART;TZID=Pacific/Honolulu:" + startTime.format());
		writer.println("DTEND;TZID=Pacific/Honolulu:" + endTime.format());
		writer.println("END:VEVENT");
		writer.println("END:VCALENDAR");
		System.out.println("Finished Creating File");
		writer.close();	
	}
	
}
