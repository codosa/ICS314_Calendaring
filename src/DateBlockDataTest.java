import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;


public class DateBlockDataTest {
  private static final String       mStart = "20151001T100000";
  private static final String       mEnd = "20151001T120000";
  private DateData                  mDate1 = new DateData(2015, 10, 1, 10, 0, 0);
  private DateData                  mDate2 = new DateData(2015, 10, 1, 12, 0, 0);
  private DateBlockData             mBlock1 = new DateBlockData(mStart, mEnd);
  private DateBlockData             mBlock2 = new DateBlockData("20151001T080000", "20151001T090000");
  private DateBlockData             mBlock3 = new DateBlockData("20141001T120000", "20141001T140000");

  @Test
  public void testDateBlockDataStringString() {
    assertTrue(mDate1.compareTo(mBlock1.getStartTime()) == 0);
    assertTrue(mDate2.compareTo(mBlock1.getEndTime()) == 0);
  }

  @Test
  public void testDifference() {
    DateBlockData diff = DateBlockData.difference(mBlock2, mBlock1);
    assertTrue(DateData.timeDifference(diff.getStartTime(), mBlock2.getEndTime()) == 0);
    assertTrue(DateData.timeDifference(diff.getEndTime(), mBlock1.getStartTime()) == 0);
    DateBlockData diff2 = DateBlockData.difference(mBlock1, mBlock2);
    assertEquals(diff2, null);
  }

  @Test
  public void testCompareTo() {
    assertTrue(mBlock1.compareTo(mBlock2) == 1);
    assertTrue(mBlock2.compareTo(mBlock1) == -1);
    assertTrue(mBlock3.compareTo(mBlock2) == -1);
  }

}
