
public class DateBlockData implements Comparable<DateBlockData> {
  private DateData mStartTime;
  private DateData mEndTime;

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

  public DateBlockData difference(DateBlockData other) {
    DateBlockData diff = new DateBlockData();
    diff.mStartTime = new DateData(mEndTime);
    diff.mEndTime = new DateData(other.mStartTime);
    if (diff.mStartTime.timeDifference(diff.mEndTime) <= 0)
      return null;
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
