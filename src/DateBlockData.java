public class DateBlockData implements Comparable<DateBlockData> {
  private DateData                mStartTime;
  private DateData                mEndTime;

  public DateBlockData() {
    mStartTime = new DateData();
    mEndTime = new DateData();
  }

  public DateBlockData(DateData start, DateData end) {
    mStartTime = start;
    mEndTime = end;
  }

  public DateBlockData(String start, String end) {
    mStartTime = new DateData(start);
    mEndTime = new DateData(end);
  }

  public static DateBlockData difference(DateBlockData left, DateBlockData right) {
    DateBlockData diff = new DateBlockData();
    diff.setStartTime(new DateData(left.getEndTime()));
    diff.setEndTime(new DateData(right.getStartTime()));
    if (DateData.timeDifference(diff.getStartTime(), diff.getEndTime()) <= 0) return null;
    return diff;
  }

  public DateData getStartTime() {
    return mStartTime;
  }

  public DateData getEndTime() {
    return mEndTime;
  }

  public void setStartTime(DateData start) {
    mStartTime = start;
  }

  public void setEndTime(DateData end) {
    mEndTime = end;
  }

  @Override
  public int compareTo(DateBlockData o) {
    return mStartTime.compareTo(o.mStartTime);
  }
}
