import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;


public class DateDataTest {
  private DateData mDate1 = new DateData(2015, 10, 1, 10, 0, 0);
  private DateData mDate2 = new DateData(2015, 10, 1, 12, 0, 0);

  @Test
  public void testDateDataString() {
    DateData testDate = new DateData("20151001T100000");
    assertTrue(testDate.compareTo(mDate1) == 0);
  }
  
  @Test
  public void testDateDataDateData() {
    DateData testDate = new DateData(mDate1);
    assertNotSame(testDate, mDate1);
  }
  
  @Test
  public void testTimeDifference() {
    int diff = DateData.timeDifference(mDate1, mDate2);
    assertTrue(diff == 7200);
  }

  @Test
  public void testIsSameDate() {
    assertTrue(DateData.isSameDate(mDate1, mDate2));
  }

  @Test
  public void testFormat() {
    assertTrue(mDate1.format().equals("20151001T100000"));
  }

  @Test
  public void testCompareTo() {
    assertTrue(mDate1.compareTo(mDate2) == -1);
  }

}
