import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class CalendarMaker {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter name for file you would like to save: ");
		String inputString = scanner.nextLine();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(inputString, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error with file name");
			return;
		}
		writer.println("BEGIN:VCALENDAR");
		writer.println("VERSION:2.0");
		writer.println("BEGIN:VEVENT");
		System.out.println("(1)Public or (2)Private?");
		int inputInt = scanner.nextInt();
		if (inputInt == 1) {
			writer.println("CLASS:PUBLIC");
		} else {
			writer.println("CLASS:PRIVATE");
		}
		System.out.println("Enter a Priority (1-9)");
		inputInt = scanner.nextInt();
		scanner.nextLine();
		if (inputInt > 9 || inputInt < 1) {
			inputInt = 9;
		}
		writer.println("PRIORITY:" + inputInt);
		System.out.println("Enter a Location");
		inputString = scanner.nextLine();
		writer.println("LOCATION:" + inputString);
		System.out.println("Enter a Summary");
		inputString = scanner.nextLine();
		writer.println("SUMMARY:" + inputString);
		writer.println("DTSTART;TZID=Pacific/Honolulu:20150508T180000");
		writer.println("DDTEND;TZID=Pacific/Honolulu:20150508T200000");
		writer.println("END:VEVENT");
		writer.println("END:VCALENDAR");
		System.out.println("Finished Creating File");
		writer.close();
	}
	
}
