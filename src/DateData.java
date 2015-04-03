import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class DateData implements Comparable<DateData> {
  private static final DateFormat           ICS_FORMAT_DATE = new SimpleDateFormat(
      "yyyyMMdd");
  private static final DateFormat           ICS_FORMAT_TIME = new SimpleDateFormat(
      "HHmmss");
  private static final DateFormat           DATE_FORMAT = new SimpleDateFormat(
      "MM/dd/yyyy");
  private static final DateFormat           TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

  private Calendar                          mCal;

  public DateData() {
    mCal = Calendar.getInstance();
  }

  public DateData(int year, int month, int day, int hour, int minute, int second) {
    mCal = Calendar.getInstance();
    mCal.set(year, month - 1, day, hour, minute, second);
  }

  public DateData(DateData other) {
    mCal = (Calendar) other.mCal.clone();
  }

  public DateData(String data) {
    int year = Integer.parseInt(data.substring(0, 4));
    int month = Integer.parseInt(data.substring(4, 6));
    int day = Integer.parseInt(data.substring(6, 8));
    int hour = Integer.parseInt(data.substring(9, 11));
    int minute = Integer.parseInt(data.substring(11, 13));
    int second = Integer.parseInt(data.substring(13, 15));
    // possibly could check if data is valid over here
    mCal = Calendar.getInstance();
    mCal.set(year, month - 1, day, hour, minute, second);
  }

  public Calendar getCal() {
    return mCal;
  }

  public void setCal(Calendar cal) {
    mCal = cal;
  }

  public static int timeDifference(DateData left, DateData right) {
    int leftSecs = left.getCal().get(Calendar.SECOND) + left.getCal().get(Calendar.MINUTE) * 60
        + left.getCal().get(Calendar.HOUR) * 3600;
    int rightSecs = right.getCal().get(Calendar.SECOND)
        + right.getCal().get(Calendar.MINUTE) * 60 + right.getCal().get(Calendar.HOUR)
        * 3600;
    return rightSecs - leftSecs;
  }

  public boolean isSameDate(DateData other) {
    return mCal.get(Calendar.MONTH) == other.mCal.get(Calendar.MONTH)
        && other.mCal.get(Calendar.YEAR) == mCal.get(Calendar.YEAR)
        && other.mCal.get(Calendar.DAY_OF_MONTH) == mCal
            .get(Calendar.DAY_OF_MONTH);
  }

  public String format() {
    return ICS_FORMAT_DATE.format(mCal.getTime()) + "T"
        + ICS_FORMAT_TIME.format(mCal.getTime());
  }

  public void show() {
    System.out.println("Month/Day/Year: " + DATE_FORMAT.format(mCal.getTime()));
    System.out.println("Hour:Minute:Second: "
        + TIME_FORMAT.format(mCal.getTime()));
  }

  public boolean grab() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter Month (1-12):");
    int inputInt = Integer.parseInt(scanner.nextLine());
    if (inputInt < 1 || inputInt > 12)
      return false;
    mCal.set(Calendar.MONTH, inputInt - 1);

    System.out.println("Enter Day (1-31):");
    inputInt = Integer.parseInt(scanner.nextLine());
    if (inputInt < 1 || inputInt > 31)
      return false;
    mCal.set(Calendar.DAY_OF_MONTH, inputInt);

    System.out.println("Enter Year (0-9999):");
    inputInt = Integer.parseInt(scanner.nextLine());
    if (inputInt < 0 || inputInt > 9999)
      return false;
    mCal.set(Calendar.YEAR, inputInt);

    System.out.println("Enter Hour (24-hr format):");
    inputInt = Integer.parseInt(scanner.nextLine());
    if (inputInt < 0 || inputInt > 23)
      return false;
    mCal.set(Calendar.HOUR_OF_DAY, inputInt);

    System.out.println("Enter Minute (0-59):");
    inputInt = Integer.parseInt(scanner.nextLine());
    if (inputInt < 0 || inputInt > 59)
      return false;
    mCal.set(Calendar.MINUTE, inputInt);

    System.out.println("Enter Second (0-59):");
    inputInt = Integer.parseInt(scanner.nextLine());
    if (inputInt < 0 || inputInt > 59)
      return false;
    mCal.set(Calendar.SECOND, inputInt);

    return true;
  }

  @Override
  public int compareTo(DateData o) {
    if (mCal.getTime().before(o.mCal.getTime())) {
      return -1;
    } else if (mCal.getTime().after(o.mCal.getTime())) {
      return 1;
    } else {
      return 0;
    }
  }
}
