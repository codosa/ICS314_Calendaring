import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

// TODO
// Test driven development
// make some functions static
// clean up code for createFreeTime
// better naming for createFreeTime
// more tests for createFreeTime 

public class CalendarMaker {
  private final static int          PUBLIC_PRIVATE = 1;
  private final static int          PRIORITY = 2;
  private final static int          LOCATION = 3;
  private final static int          SUMMARY = 4;
  private final static int          START_TIME = 5;
  private final static int          END_TIME = 6;
  private final static int          CREATE_FREE_TIME = 7;
  private final static int          SHOW_DATA = 8;
  private final static int          MAKE_CALENDAR = 9;
  private final static int          QUIT = 10;

  private boolean                   mIsPublic;
  private int                       mPriority;
  private String                    mLocation;
  private String                    mSummary;

  private DateBlockData             mDateBlock;
  private Scanner                   mScanner;

  public static void main(String[] args) {
    CalendarMaker maker = new CalendarMaker();
    maker.start();
  }

  public CalendarMaker() {
    mScanner = new Scanner(System.in);
    mIsPublic = false;
    mPriority = 1;
    mLocation = new String("");
    mSummary = new String("");
    mDateBlock = new DateBlockData();
  }

  public void start() {
    int inputInt = 0;
    boolean test = true;
    while (inputInt != QUIT) {
      System.out.println("(1)Public/Private, (2)Priority, (3)Location, "
          + "(4)Summary, (5)Start, (6)End, (7)Create Free Time, (8)Show Data, "
          + "(9)Make Calendar, (10)Quit");
      try {
        inputInt = Integer.parseInt(mScanner.nextLine());
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
        System.out
            .println("Enter filenames separated by ',' (test.txt,test2.txt): ");
        createFreeTime(mScanner.nextLine().split(","));
        break;
      case SHOW_DATA:
        showCurrentData();
        break;
      case MAKE_CALENDAR:
        System.out.println("Enter a filename to save data");
        String filename = mScanner.nextLine();
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
    ArrayList<DateBlockData> mDateBlocks = new ArrayList<DateBlockData>();
    for (String filename : filenames) {
      if (validFile(filename)) {
        File file = new File(filename);
        Scanner fileScanner = null;
        try {
          fileScanner = new Scanner(file);
        } catch (Exception e) {
        }
        String start = null;
        String end = null;
        // parse .ics for DTSTART and DTEND and init objects
        while (fileScanner.hasNext()) {
          String[] splitLine = fileScanner.nextLine().split(":");
          if (stringMatch(splitLine[0], "DTSTART"))
            start = splitLine[1];
          if (stringMatch(splitLine[0], "DTEND"))
            end = splitLine[1];
        }
        // if the .ics file has proper DTSTART and DTEND
        if (start != null && end != null) {
          DateBlockData block = new DateBlockData(start, end);
          block.getStartTime().show();
          block.getEndTime().show();
          // all date blocks must be on the same day, only add if they are
          if (mDateBlocks.isEmpty() || (!mDateBlocks.isEmpty() && 
              DateData.isSameDate(mDateBlocks.get(0).getStartTime(), block.getStartTime()))) {
            mDateBlocks.add(block);
          }
        }
      }
    }
    if (mDateBlocks.size() == 0) {
      return;
    }
    int year = mDateBlocks.get(0).getStartTime().getCal().get(Calendar.YEAR);
    // add one because cal.get(Calendar.MONTH) starts at 0 for jan
    int month = mDateBlocks.get(0).getStartTime().getCal().get(Calendar.MONTH) + 1;
    int day = mDateBlocks.get(0).getStartTime().getCal()
        .get(Calendar.DAY_OF_MONTH);
    mDateBlocks.add(new DateBlockData(new DateData(year, month, day, 0, 0, 0),
        new DateData(year, month, day, 0, 0, 0)));
    mDateBlocks.add(new DateBlockData(
        new DateData(year, month, day, 23, 59, 59), new DateData(year, month,
            day, 23, 59, 59)));
    Collections.sort(mDateBlocks);
    mSummary = "Free Time";
    for (int i = 1; i < mDateBlocks.size(); i++) {
      DateBlockData temp = DateBlockData.difference(mDateBlocks.get(i-1), 
          mDateBlocks.get(i));
      if (temp != null) {
        mDateBlock = temp;
        makeCalendar(Integer.toString(i) + ".ics");
      }
    }
  }

  public boolean grabPublic() {
    System.out.println("(1)Public or (2)Private?");
    int inputInt = Integer.parseInt(mScanner.nextLine());
    if (inputInt != 1 && inputInt != 2)
      return false;
    if (inputInt == 1) {
      mIsPublic = true;
    } else {
      mIsPublic = false;
    }
    return true;
  }

  public boolean grabPriority() {
    System.out.println("Enter a Priority (1-9)");
    int inputInt = Integer.parseInt(mScanner.nextLine());
    if (inputInt < 1 || inputInt > 9)
      return false;
    mPriority = inputInt;
    return true;
  }

  public void grabLocation() {
    System.out.println("Enter a Location");
    mLocation = mScanner.nextLine();
  }

  public void grabSummary() {
    System.out.println("Enter a Summary");
    mSummary = mScanner.nextLine();
  }

  public boolean grabStart() {
    System.out.println("------StartTime------");
    return mDateBlock.getStartTime().grab();
  }

  public boolean grabEnd() {
    System.out.println("-------EndTime-------");
    return mDateBlock.getEndTime().grab();
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
    System.out.println("Public: " + Boolean.toString(mIsPublic));
  }

  public void showPriority() {
    System.out.println("Priority: " + mPriority);
  }

  public void showLocation() {
    System.out.println("Location: " + mLocation);
  }

  public void showSummary() {
    System.out.println("Summary: " + mSummary);
  }

  public void showStartTime() {
    System.out.println("------StartTime------");
    mDateBlock.getStartTime().show();
    System.out.println("---------------------");
  }

  public void showEndTime() {
    System.out.println("-------EndTime-------");
    mDateBlock.getEndTime().show();
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
    if (mIsPublic) {
      writer.println("CLASS:PUBLIC");
    } else {
      writer.println("CLASS:PRIVATE");
    }
    writer.println("PRIORITY:" + mPriority);
    writer.println("LOCATION:" + mLocation);
    writer.println("SUMMARY:" + mSummary);
    writer.println("DTSTART;TZID=Pacific/Honolulu:"
        + mDateBlock.getStartTime().format());
    writer.println("DTEND;TZID=Pacific/Honolulu:"
        + mDateBlock.getEndTime().format());
    writer.println("END:VEVENT");
    writer.println("END:VCALENDAR");
    System.out.println("Finished Creating File: " + filename);
    writer.close();
  }

  private boolean validFile(String filename) {
    File file = new File(filename);
    if (file.exists() && !file.isDirectory())
      return true;
    return false;
  }

  private boolean stringMatch(String string, String pattern) {
    if (string.isEmpty() || pattern.isEmpty()
        || pattern.length() > string.length())
      return false;
    for (int i = 0; i < pattern.length(); i++) {
      if (string.charAt(i) != pattern.charAt(i))
        return false;
    }
    return true;
  }

}
