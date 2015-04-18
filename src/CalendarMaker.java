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
  private final static int PUBLIC_PRIVATE = 1;
  private final static int PRIORITY = 2;
  private final static int LOCATION = 3;
  private final static int SUMMARY = 4;
  private final static int START_TIME = 5;
  private final static int END_TIME = 6;
  private final static int CREATE_FREE_TIME = 7;
  private final static int CREATE_MEETING_TIME = 8;
  private final static int SHOW_DATA = 9;
  private final static int MAKE_CALENDAR = 10;
  private final static int QUIT = 11;

  private boolean mIsPublic;
  private int mPriority;
  private String mLocation;
  private String mSummary;

  private DateBlockData mDateBlock;
  private Scanner mScanner;

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
          + "(4)Summary, (5)Start, (6)End, (7)Create Free Time, (8)Create Meeting Times, " +
          "(9)Show Data, (10)Make Calendar, (11)Quit");
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
        createFreeTimes(mScanner.nextLine().split(","));
        break;
      case CREATE_MEETING_TIME:
        System.out
            .println("Enter first list of files separated by ',' (test.txt,test2.txt): ");
        String[] list1 = mScanner.nextLine().split(",");
        System.out
            .println("Enter second list of files separated by ',' (test.txt,test2.txt): ");
        String[] list2 = mScanner.nextLine().split(",");
        createMeetingTimes(list1, list2);
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

  public void createFreeTimes(String[] filenames) {
    ArrayList<DateBlockData> dateBlocks = fileListToDateBlocks(filenames);
    if (dateBlocks.size() == 0) return;
    mSummary = "Free Time";
    addBeginEndTimes(dateBlocks);
    Collections.sort(dateBlocks);
    createFreeTimeFromDateBlocks(dateBlocks);
  }
  
  public void createMeetingTimes(String[] list1, String[] list2) {
    ArrayList<DateBlockData> dateBlocks1 = fileListToDateBlocks(list1);
    ArrayList<DateBlockData> dateBlocks2 = fileListToDateBlocks(list2);
    if (dateBlocks1.size() > 0 && dateBlocks2.size() > 0) {
      // could make this more robust and check all dates
      if (!DateData.isSameDate(dateBlocks1.get(0).getStartTime(), dateBlocks2.get(0).getStartTime())) {
        System.out.println("Lists are not same Dates");
        return;
      }
    }
    ArrayList<DateBlockData> allDateBlocks = new ArrayList<DateBlockData>(dateBlocks1);
    allDateBlocks.addAll(dateBlocks2);
    if (allDateBlocks.size() == 0) return;
    mSummary = "POSSIBLE MEETING TIME";
    addBeginEndTimes(allDateBlocks); 
    Collections.sort(allDateBlocks);
    for (DateBlockData data : allDateBlocks) {
      data.getStartTime().show();
      data.getEndTime().show();
    }
    createFreeTimeFromDateBlocks(allDateBlocks);
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
  
  private void createFreeTimeFromDateBlocks(ArrayList<DateBlockData> dateBlocks) {
    int fileIndex = 1;
    int currentBlockIndex = 0;
    for (int i = 1; i < dateBlocks.size(); i++) {
      DateBlockData temp = DateBlockData.difference(
          dateBlocks.get(currentBlockIndex), dateBlocks.get(i));
      // if not overlapping blocks make a free time with the difference
      if (temp != null) {
        mDateBlock = temp;
        currentBlockIndex = i;
        makeCalendar(Integer.toString(fileIndex++) + ".ics");
      } else {
        // if the end of the second block is later than the first, we increase
        // the first ones to the second
        if (DateData.timeDifference(dateBlocks.get(currentBlockIndex)
            .getEndTime(), dateBlocks.get(i).getEndTime()) > 0) {
          dateBlocks.get(currentBlockIndex).setEndTime(
              new DateData(dateBlocks.get(i).getEndTime()));
        }
      }
    }   
  }
  
  // makes array of DateBlocks that have the same date
  private ArrayList<DateBlockData> fileListToDateBlocks(String[] filenames) {
    ArrayList<DateBlockData> dateBlocks = new ArrayList<DateBlockData>();
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
          // all date blocks must be on the same day, only add if they are
          if (dateBlocks.isEmpty()
              || (!dateBlocks.isEmpty() && DateData.isSameDate(dateBlocks
                  .get(0).getStartTime(), block.getStartTime()))) {
            dateBlocks.add(block);
          }
        }
      }
    }   
    return dateBlocks;
  }
  
  private void addBeginEndTimes(ArrayList<DateBlockData> list) {
    int year = list.get(0).getStartTime().getCal().get(Calendar.YEAR);
    // add one because cal.get(Calendar.MONTH) starts at 0 for jan
    int month = list.get(0).getStartTime().getCal().get(Calendar.MONTH) + 1;
    int day = list.get(0).getStartTime().getCal()
        .get(Calendar.DAY_OF_MONTH);
    list.add(new DateBlockData(new DateData(year, month, day, 0, 0, 0),
        new DateData(year, month, day, 0, 0, 0)));
    list.add(new DateBlockData(
        new DateData(year, month, day, 23, 59, 59), new DateData(year, month,
            day, 23, 59, 59)));   
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
