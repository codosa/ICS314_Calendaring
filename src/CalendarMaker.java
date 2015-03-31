import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;


// TODO
// Test driven development
// separate into multiple classes
// make some functions static
// clean up code for createFreeTime
// better naming for createFreeTime
// more tests for createFreeTime 
// change makeCalendar to take in a DateBlockData

public class CalendarMaker {
	private static final DateFormat ICS_FORMAT_DATE = new SimpleDateFormat("yyyyMMdd");
	private static final DateFormat ICS_FORMAT_TIME = new SimpleDateFormat("HHmmss");
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	private final static int 		    PUBLIC_PRIVATE = 1;
	private final static int 		    PRIORITY = 2;
	private final static int 		    LOCATION = 3;
	private final static int 		    SUMMARY = 4;
	private final static int 		    START_TIME = 5;
	private final static int 		    END_TIME = 6;
	private final static int		    CREATE_FREE_TIME = 7;
	private final static int 		    SHOW_DATA = 8;
	private final static int 		    MAKE_CALENDAR = 9;
	private final static int 		    QUIT = 10;
	
	private boolean 				        isPublic;
	private int 					          priority;
	private String					        location;
	private String					        summary;

	private class DateData implements Comparable<DateData> {
		public Calendar cal;

		public DateData() {
			cal = Calendar.getInstance();
		}
		
		public DateData(int year, int month, int day, int hour, int minute, int second) {
		  cal = Calendar.getInstance();
		  cal.set(year, month-1, day, hour, minute, second);
		}
		
		public DateData(DateData other) {
		  cal = (Calendar) other.cal.clone();
		}
		
		public DateData(String data) {
			int year = Integer.parseInt(data.substring(0, 4));
			int month = Integer.parseInt(data.substring(4, 6));
			int day = Integer.parseInt(data.substring(6, 8));
			int hour = Integer.parseInt(data.substring(9, 11));
			int minute = Integer.parseInt(data.substring(11, 13));
			int second = Integer.parseInt(data.substring(13, 15));
			// possibly could check if data is valid over here
			cal = Calendar.getInstance();
			cal.set(year, month-1, day, hour, minute, second);
		}
		
		public int timeDifference(DateData other) {
		  int leftSecs = cal.get(Calendar.SECOND) + cal.get(Calendar.MINUTE)*60 +
		      cal.get(Calendar.HOUR)*3600;
		  int rightSecs = other.cal.get(Calendar.SECOND) + other.cal.get(Calendar.MINUTE)*60 +
		      other.cal.get(Calendar.HOUR)*3600;
		  return rightSecs - leftSecs;
		}

		public boolean isSameDate(DateData other) {
			return cal.get(Calendar.MONTH) == other.cal.get(Calendar.MONTH) && 
					other.cal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && 
					other.cal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH);
		}
		
		public String format() {
			return ICS_FORMAT_DATE.format(cal.getTime()) + "T" + ICS_FORMAT_TIME.format(cal.getTime());
		}

		public void show() {
			System.out.println("Month/Day/Year: " + DATE_FORMAT.format(cal.getTime()));
			System.out.println("Hour:Minute:Second: " + TIME_FORMAT.format(cal.getTime()));
		}
		
		public boolean grab() {
			System.out.println("Enter Month (1-12):");
			int inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 1 || inputInt > 12) return false;
			cal.set(Calendar.MONTH, inputInt-1);

			System.out.println("Enter Day (1-31):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 1 || inputInt > 31) return false;
			cal.set(Calendar.DAY_OF_MONTH, inputInt);

			System.out.println("Enter Year (0-9999):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 9999) return false;
			cal.set(Calendar.YEAR, inputInt);

			System.out.println("Enter Hour (24-hr format):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 23) return false;
			cal.set(Calendar.HOUR_OF_DAY, inputInt);
			
			System.out.println("Enter Minute (0-59):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 59) return false;
			cal.set(Calendar.MINUTE, inputInt);

			System.out.println("Enter Second (0-59):");
			inputInt = Integer.parseInt(scanner.nextLine());
			if (inputInt < 0 || inputInt > 59) return false;
			cal.set(Calendar.SECOND, inputInt);

			return true;
		}


		@Override
		public int compareTo(DateData o) {
			if (cal.getTime().before(o.cal.getTime())) {
				return -1;
			} else if (cal.getTime().after(o.cal.getTime())) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	private class DateBlockData implements Comparable<DateBlockData> {
		public DateData			startTime;
		public DateData			endTime;
		
		public DateBlockData() {
			startTime = new DateData();
			endTime = new DateData();
		}
		
		public DateBlockData(DateData start, DateData end) {
		  startTime = start;
		  endTime = end;
		}
		
		public DateBlockData(String start, String end) {
			startTime = new DateData(start);
			endTime = new DateData(end);
		}
		
		public DateBlockData difference(DateBlockData other) {
		  DateBlockData diff = new DateBlockData();
		  diff.startTime = new DateData(endTime);
		  diff.endTime = new DateData(other.startTime);
		  if (diff.startTime.timeDifference(diff.endTime) <= 0) return null;
		  return diff;
		}

		@Override
		public int compareTo(DateBlockData o) {
			return startTime.compareTo(o.startTime);
		}
	}
	
	private DateBlockData 			dateBlock;	
	private Scanner					    scanner;
	
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
		dateBlock = new DateBlockData();
	}
	
	public void start() {
		int inputInt = 0;
		boolean test = true;
		while (inputInt != QUIT) {
			System.out.println("(1)Public/Private, (2)Priority, (3)Location, " +
					"(4)Summary, (5)Start, (6)End, (7)Create Free Time, (8)Show Data, " +
					"(9)Make Calendar, (10)Quit");
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
			case CREATE_FREE_TIME:
				System.out.println("Enter filenames separated by ',' (test.txt,test2.txt): ");
				createFreeTime(scanner.nextLine().split(","));
				break;
			case SHOW_DATA:
				showCurrentData();
				break;
			case MAKE_CALENDAR:
				System.out.println("Enter a filename to save data");
				String filename = scanner.nextLine();
				makeCalendar(filename);
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
	
	public void createFreeTime(String[] filenames) {
		ArrayList<DateBlockData> dateBlocks = new ArrayList<DateBlockData>();
		for (String filename : filenames) {
			if (validFile(filename)) {
				File file = new File(filename);
				Scanner fileScanner = null;
				try {
					fileScanner = new Scanner(file);
				} catch (Exception e) {}
				String start = null;
				String end = null;
				// parse .ics for DTSTART and DTEND and init objects
				while (fileScanner.hasNext()) {
					String[] splitLine = fileScanner.nextLine().split(":");
					if (stringMatch(splitLine[0], "DTSTART")) start = splitLine[1];
					if (stringMatch(splitLine[0], "DTEND")) end = splitLine[1];
				}
				// if the .ics file has proper DTSTART and DTEND
				if (start != null && end != null) {
					DateBlockData block = new DateBlockData(start, end);
					block.startTime.show();
					block.endTime.show();
					// all date blocks must be on the same day, only add if they are
					if (dateBlocks.isEmpty() || (!dateBlocks.isEmpty() && 
							dateBlocks.get(0).startTime.isSameDate(block.startTime))) {
						dateBlocks.add(block);
					}
				}
			}
		}
		if (dateBlocks.size() == 0) {
		  return;
		}
		int year = dateBlocks.get(0).startTime.cal.get(Calendar.YEAR);
		// add one because cal.get(Calendar.MONTH) starts at 0 for jan
		int month = dateBlocks.get(0).startTime.cal.get(Calendar.MONTH) + 1;
		int day = dateBlocks.get(0).startTime.cal.get(Calendar.DAY_OF_MONTH);
		dateBlocks.add(new DateBlockData(new DateData(year, month, day, 0, 0, 0), 
		    new DateData(year, month, day, 0, 0, 0)));
		dateBlocks.add(new DateBlockData(new DateData(year, month, day, 23, 59, 59), 
		    new DateData(year, month, day, 23, 59, 59)));
		Collections.sort(dateBlocks);
		for (int i = 1; i < dateBlocks.size(); i++) {
		  DateBlockData temp = dateBlocks.get(i-1).difference(dateBlocks.get(i));
		  if (temp != null) {
		    dateBlock = temp;
		    makeCalendar(Integer.toString(i));
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
		return dateBlock.startTime.grab();
	}
	
	public boolean grabEnd() {
		System.out.println("-------EndTime-------");
		return dateBlock.endTime.grab();
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
		dateBlock.startTime.show();
		System.out.println("---------------------");
	}
	
	public void showEndTime() {
		System.out.println("-------EndTime-------");
		dateBlock.endTime.show();
		System.out.println("---------------------");
	}
	
	public void makeCalendar(String filename) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filename, "UTF-8");
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
		writer.println("DTSTART;TZID=Pacific/Honolulu:" + dateBlock.startTime.format());
		writer.println("DTEND;TZID=Pacific/Honolulu:" + dateBlock.endTime.format());
		writer.println("END:VEVENT");
		writer.println("END:VCALENDAR");
		System.out.println("Finished Creating File");
		writer.close();	
	}
	
	private boolean validFile(String filename) {
		File file = new File(filename);
		if (file.exists() && !file.isDirectory()) return true;
		return false;
	}
	
	private boolean stringMatch(String string, String pattern) {
		if (string.isEmpty() || pattern.isEmpty() || pattern.length() > string.length()) return false;
		for (int i = 0; i < pattern.length(); i++) {
			if (string.charAt(i) != pattern.charAt(i)) return false;
		}
		return true;
	}
	
}
