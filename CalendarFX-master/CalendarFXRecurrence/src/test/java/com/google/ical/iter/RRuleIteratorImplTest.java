/**
 * Copyright (C) 2015, 2016 Dirk Lemmermann Software & Consulting (dlsc.com)
 * <p>
 * This file is part of CalendarFX.
 */

// Copyright (C) 2006 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.ical.iter;

import com.google.ical.util.DTBuilder;
import com.google.ical.util.TimeUtils;
import com.google.ical.values.DateValue;
import com.google.ical.values.IcalParseUtil;
import com.google.ical.values.PeriodValue;
import com.google.ical.values.RRule;
import junit.framework.TestCase;

import java.util.TimeZone;

/**
 * @author mikesamuel+svn@gmail.com (Mike Samuel)
 */
public class RRuleIteratorImplTest extends TestCase {

    static final TimeZone PST = TimeZone.getTimeZone("America/Los_Angeles");
    static final TimeZone UTC = TimeUtils.utcTimezone();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void runRecurrenceIteratorTest(
            String rruleText, DateValue dtStart, int limit, String golden)
            throws Exception {
        runRecurrenceIteratorTest(rruleText, dtStart, limit, golden, null, UTC);
    }

    private void runRecurrenceIteratorTest(
            String rruleText, DateValue dtStart, int limit, String golden,
            DateValue advanceTo)
            throws Exception {
        runRecurrenceIteratorTest(
                rruleText, dtStart, limit, golden, advanceTo, UTC);
    }

    private void runRecurrenceIteratorTest(
            String rruleText, DateValue dtStart, int limit, String golden,
            TimeZone tz)
            throws Exception {
        runRecurrenceIteratorTest(rruleText, dtStart, limit, golden, null, tz);
    }

    private void runRecurrenceIteratorTest(
            String rruleText, DateValue dtStart, int limit, String golden,
            DateValue advanceTo, TimeZone tz)
            throws Exception {
        RecurrenceIterator ri = RecurrenceIteratorFactory.createRecurrenceIterator(
                new RRule(rruleText), dtStart, tz);
        if (null != advanceTo) {
            ri.advanceTo(advanceTo);
        }
        StringBuilder sb = new StringBuilder();
        int k = 0, n = limit;
        while (ri.hasNext() && --n >= 0) {
            if (k++ != 0) {
                sb.append(',');
            }
            sb.append(ri.next());
        }
        if (n < 0) {
            sb.append(",...");
        }
        assertEquals(golden, sb.toString());

        if (null == advanceTo) {
            runRecurrenceIteratorTest(rruleText, dtStart, limit, golden, dtStart, tz);
        }
    }

    public void testFrequencyLimits() throws Exception {
        RecurrenceIteratorFactory.createRecurrenceIterator(
                new RRule(
                        "RRULE:FREQ=SECONDLY;BYSECOND=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,"
                                + "15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,"
                                + "30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,"
                                + "45,46,47,48,49,50,51,52,53,54,55,56,57,58,59"),
                IcalParseUtil.parseDateValue("20000101"), TimeUtils.utcTimezone());
    }

    public void testSimpleDaily() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY", IcalParseUtil.parseDateValue("20060120"), 5,
                "20060120,20060121,20060122,20060123,20060124,...");
    }

    public void testSimpleWeekly() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY", IcalParseUtil.parseDateValue("20060120"), 5,
                "20060120,20060127,20060203,20060210,20060217,...");
    }

    public void testSimpleMonthly() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY", IcalParseUtil.parseDateValue("20060120"), 5,
                "20060120,20060220,20060320,20060420,20060520,...");
    }

    public void testSimpleYearly() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY", IcalParseUtil.parseDateValue("20060120"), 5,
                "20060120,20070120,20080120,20090120,20100120,...");
    }

    // from section 4.3.10
    public void testMultipleByParts() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU",
                IcalParseUtil.parseDateValue("19970105"), 8,
                "19970105,19970112,19970119,19970126," +
                        "19990103,19990110,19990117,19990124,...");
    }

    public void testCountWithInterval() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;COUNT=10;INTERVAL=2",
                IcalParseUtil.parseDateValue("19970105"), 11,
                "19970105,19970107,19970109,19970111,19970113," +
                        "19970115,19970117,19970119,19970121,19970123");
    }

    // from section 4.6.5
    public void testNegativeOffsets() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10",
                IcalParseUtil.parseDateValue("19970105"), 5,
                "19971026,19981025,19991031,20001029,20011028,...");
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=4",
                IcalParseUtil.parseDateValue("19970105"), 5,
                "19970406,19980405,19990404,20000402,20010401,...");
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=4;UNTIL=19980404T150000Z",
                IcalParseUtil.parseDateValue("19970105"), 5, "19970406");
    }

    // from section 4.8.5.4
    public void testDailyFor10Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;COUNT=10",
                IcalParseUtil.parseDateValue("19970902T090000"), 11,
                "19970902T090000,19970903T090000,19970904T090000,19970905T090000," +
                        "19970906T090000,19970907T090000,19970908T090000,19970909T090000," +
                        "19970910T090000,19970911T090000");

    }

    public void testDailyUntilDec4() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;UNTIL=19971204",
                IcalParseUtil.parseDateValue("19971128"), 11,
                "19971128,19971129,19971130,19971201,19971202,19971203,19971204");
    }

    public void testEveryOtherDayForever() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;INTERVAL=2",
                IcalParseUtil.parseDateValue("19971128"), 5,
                "19971128,19971130,19971202,19971204,19971206,...");
    }

    public void testEvery10Days5Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5",
                IcalParseUtil.parseDateValue("19970902"), 5,
                "19970902,19970912,19970922,19971002,19971012");
    }

    public String goldenDateRange(String dateStr) throws Exception {
        return goldenDateRange(dateStr, 1);
    }

    public String goldenDateRange(String dateStr, int interval) throws Exception {
        PeriodValue period = IcalParseUtil.parsePeriodValue(dateStr);
        DTBuilder b = new DTBuilder(period.start());
        StringBuilder out = new StringBuilder();
        while (true) {
            DateValue d = b.toDate();
            if (d.compareTo(period.end()) > 0) {
                break;
            }
            if (0 != out.length()) {
                out.append(',');
            }
            out.append(d);
            b.day += interval;
        }
        return out.toString();
    }

    public void testEveryDayInJanuaryFor3Years() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;UNTIL=20000131T090000Z;\n" +
                        " BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA",
                IcalParseUtil.parseDateValue("19980101"), 100,
                goldenDateRange("19980101/19980131") + ","
                        + goldenDateRange("19990101/19990131") + ","
                        + goldenDateRange("20000101/20000131"));
    }

    public void testWeeklyFor10Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;COUNT=10",
                IcalParseUtil.parseDateValue("19970902"), 10,
                "19970902,19970909,19970916,19970923,19970930," +
                        "19971007,19971014,19971021,19971028,19971104");
    }

    public void testWeeklyUntilDec24() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;UNTIL=19971224",
                IcalParseUtil.parseDateValue("19970902"), 25,
                "19970902,19970909,19970916,19970923,19970930," +
                        "19971007,19971014,19971021,19971028,19971104," +
                        "19971111,19971118,19971125,19971202,19971209," +
                        "19971216,19971223");
    }

    public void testEveryOtherWeekForever() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=SU",
                IcalParseUtil.parseDateValue("19970902"), 11,
                "19970902,19970916,19970930,19971014,19971028," +
                        "19971111,19971125,19971209,19971223,19980106," +
                        "19980120,...");
    }

    public void testWeeklyOnTuesdayAndThursdayFor5Weeks() throws Exception {
        // if UNTIL date does not match start date, then until date treated as
        // occurring on midnight.
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;UNTIL=19971007;WKST=SU;BYDAY=TU,TH",
                IcalParseUtil.parseDateValue("19970902T090000"), 11,
                "19970902T090000,19970904T090000,19970909T090000,19970911T090000," +
                        "19970916T090000,19970918T090000,19970923T090000,19970925T090000," +
                        "19970930T090000,19971002T090000");
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH",
                IcalParseUtil.parseDateValue("19970902T090000"), 11,
                "19970902T090000,19970904T090000,19970909T090000,19970911T090000," +
                        "19970916T090000,19970918T090000,19970923T090000,19970925T090000," +
                        "19970930T090000,19971002T090000");
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH",
                IcalParseUtil.parseDateValue("19970902"), 11,
                "19970902,19970904,19970909,19970911,19970916," +
                        "19970918,19970923,19970925,19970930,19971002");
    }

    public void testEveryOtherWeekOnMWFUntilDec24() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;\n" +
                        " BYDAY=MO,WE,FR",
                IcalParseUtil.parseDateValue("19970903T090000"), 25,
                "19970903T090000,19970905T090000,19970915T090000,19970917T090000," +
                        "19970919T090000,19970929T090000,19971001T090000,19971003T090000," +
                        "19971013T090000,19971015T090000,19971017T090000,19971027T090000," +
                        "19971029T090000,19971031T090000,19971110T090000,19971112T090000," +
                        "19971114T090000,19971124T090000,19971126T090000,19971128T090000," +
                        "19971208T090000,19971210T090000,19971212T090000,19971222T090000");

        // if the UNTIL date is timed, when the start is not, the time should be
        // ignored, so we get one more instance
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;\n" +
                        " BYDAY=MO,WE,FR",
                IcalParseUtil.parseDateValue("19970903"), 25,
                "19970903,19970905,19970915,19970917," +
                        "19970919,19970929,19971001,19971003," +
                        "19971013,19971015,19971017,19971027," +
                        "19971029,19971031,19971110,19971112," +
                        "19971114,19971124,19971126,19971128," +
                        "19971208,19971210,19971212,19971222," +
                        "19971224");

        // test with an alternate timezone
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T090000Z;WKST=SU;\n" +
                        " BYDAY=MO,WE,FR",
                IcalParseUtil.parseDateValue("19970903T090000"), 25,
                "19970903T160000,19970905T160000,19970915T160000,19970917T160000," +
                        "19970919T160000,19970929T160000,19971001T160000,19971003T160000," +
                        "19971013T160000,19971015T160000,19971017T160000,19971027T170000," +
                        "19971029T170000,19971031T170000,19971110T170000,19971112T170000," +
                        "19971114T170000,19971124T170000,19971126T170000,19971128T170000," +
                        "19971208T170000,19971210T170000,19971212T170000,19971222T170000",
                PST);
    }

    public void testEveryOtherWeekOnTuThFor8Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH",
                IcalParseUtil.parseDateValue("19970902"), 8,
                "19970902,19970904,19970916,19970918,19970930," +
                        "19971002,19971014,19971016");
    }

    public void testMonthlyOnThe1stFridayFor10Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;COUNT=10;BYDAY=1FR",
                IcalParseUtil.parseDateValue("19970905"), 10,
                "19970905,19971003,19971107,19971205,19980102," +
                        "19980206,19980306,19980403,19980501,19980605");
    }

    public void testMonthlyOnThe1stFridayUntilDec24() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR",
                IcalParseUtil.parseDateValue("19970905"), 4,
                "19970905,19971003,19971107,19971205");
    }

    public void testEveryOtherMonthOnThe1stAndLastSundayFor10Occ()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU",
                IcalParseUtil.parseDateValue("19970907"), 10,
                "19970907,19970928,19971102,19971130,19980104," +
                        "19980125,19980301,19980329,19980503,19980531");
    }

    public void testMonthlyOnTheSecondToLastMondayOfTheMonthFor6Months()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO",
                IcalParseUtil.parseDateValue("19970922"), 6,
                "19970922,19971020,19971117,19971222,19980119," +
                        "19980216");
    }

    public void testMonthlyOnTheThirdToLastDay() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYMONTHDAY=-3",
                IcalParseUtil.parseDateValue("19970928"), 6,
                "19970928,19971029,19971128,19971229,19980129,19980226,...");
    }

    public void testMonthlyOnThe2ndAnd15thFor10Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=2,15",
                IcalParseUtil.parseDateValue("19970902"), 10,
                "19970902,19970915,19971002,19971015,19971102," +
                        "19971115,19971202,19971215,19980102,19980115");
    }

    public void testMonthlyOnTheFirstAndLastFor10Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1",
                IcalParseUtil.parseDateValue("19970930"), 10,
                "19970930,19971001,19971031,19971101,19971130," +
                        "19971201,19971231,19980101,19980131,19980201");
    }

    public void testEvery18MonthsOnThe10thThru15thFor10Occ() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,\n" +
                        " 15",
                IcalParseUtil.parseDateValue("19970910"), 10,
                "19970910,19970911,19970912,19970913,19970914," +
                        "19970915,19990310,19990311,19990312,19990313");
    }

    public void testEveryTuesdayEveryOtherMonth() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TU",
                IcalParseUtil.parseDateValue("19970902"), 18,
                "19970902,19970909,19970916,19970923,19970930," +
                        "19971104,19971111,19971118,19971125,19980106," +
                        "19980113,19980120,19980127,19980303,19980310," +
                        "19980317,19980324,19980331,...");
    }

    public void testYearlyInJuneAndJulyFor10Occurrences() throws Exception {
        // Note: Since none of the BYDAY, BYMONTHDAY or BYYEARDAY components
        // are specified, the day is gotten from DTSTART
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;COUNT=10;BYMONTH=6,7",
                IcalParseUtil.parseDateValue("19970610"), 10,
                "19970610,19970710,19980610,19980710,19990610," +
                        "19990710,20000610,20000710,20010610,20010710");
    }

    public void testEveryOtherYearOnJanuaryFebruaryAndMarchFor10Occurrences()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3",
                IcalParseUtil.parseDateValue("19970310"), 10,
                "19970310,19990110,19990210,19990310,20010110," +
                        "20010210,20010310,20030110,20030210,20030310");
    }

    public void testEvery3rdYearOnThe1st100thAnd200thDayFor10Occurrences()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200",
                IcalParseUtil.parseDateValue("19970101"), 10,
                "19970101,19970410,19970719,20000101,20000409," +
                        "20000718,20030101,20030410,20030719,20060101");
    }

    public void testEvery20thMondayOfTheYearForever() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=20MO",
                IcalParseUtil.parseDateValue("19970519"), 3,
                "19970519,19980518,19990517,...");
    }

    public void testMondayOfWeekNumber20WhereTheDefaultStartOfTheWeekIsMonday()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO",
                IcalParseUtil.parseDateValue("19970512"), 3,
                "19970512,19980511,19990517,...");
    }

    public void testEveryThursdayInMarchForever() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=TH",
                IcalParseUtil.parseDateValue("19970313"), 11,
                "19970313,19970320,19970327,19980305,19980312," +
                        "19980319,19980326,19990304,19990311,19990318," +
                        "19990325,...");
    }

    public void testEveryThursdayButOnlyDuringJuneJulyAndAugustForever()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8",
                IcalParseUtil.parseDateValue("19970605"), 39,
                "19970605,19970612,19970619,19970626,19970703," +
                        "19970710,19970717,19970724,19970731,19970807," +
                        "19970814,19970821,19970828,19980604,19980611," +
                        "19980618,19980625,19980702,19980709,19980716," +
                        "19980723,19980730,19980806,19980813,19980820," +
                        "19980827,19990603,19990610,19990617,19990624," +
                        "19990701,19990708,19990715,19990722,19990729," +
                        "19990805,19990812,19990819,19990826,...");
    }

    public void testEveryFridayThe13thForever() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13",
                IcalParseUtil.parseDateValue("19970902"), 5,
                "19980213,19980313,19981113,19990813,20001013," +
                        "...");
    }

    public void testTheFirstSaturdayThatFollowsTheFirstSundayOfTheMonthForever()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13",
                IcalParseUtil.parseDateValue("19970913"), 10,
                "19970913,19971011,19971108,19971213,19980110," +
                        "19980207,19980307,19980411,19980509,19980613," +
                        "...");
    }

    public void testEvery4YearsThe1stTuesAfterAMonInNovForever()
            throws Exception {
        // US Presidential Election Day
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,\n" +
                        " 5,6,7,8",
                IcalParseUtil.parseDateValue("19961105"), 3,
                "19961105,20001107,20041102,...");
    }

    public void testThe3rdInstanceIntoTheMonthOfOneOfTuesWedThursForNext3Months()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3",
                IcalParseUtil.parseDateValue("19970904"), 3,
                "19970904,19971007,19971106");
    }

    public void testThe2ndToLastWeekdayOfTheMonth() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2",
                IcalParseUtil.parseDateValue("19970929"), 7,
                "19970929,19971030,19971127,19971230,19980129," +
                        "19980226,19980330,...");
    }

    public void testEvery3HoursFrom900AmTo500PmOnASpecificDay() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=HOURLY;INTERVAL=3;UNTIL=19970903T090000Z",
                IcalParseUtil.parseDateValue("19970902T090000"), 50,
                "19970902T090000,19970902T120000,19970902T150000," +
                        "19970902T180000,19970902T210000,19970903T000000," +
                        "19970903T030000,19970903T060000,19970903T090000");
    }

    public void testEvery15MinutesFor6Occurrences() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MINUTELY;INTERVAL=15;COUNT=6",
                IcalParseUtil.parseDateValue("19970902T090000"), 13,
                "19970902T090000,19970902T091500,19970902T093000,19970902T094500," +
                        "19970902T100000,19970902T101500");
    }

    public void testEveryHourAndAHalfFor4Occurrences() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MINUTELY;INTERVAL=90;COUNT=4",
                IcalParseUtil.parseDateValue("19970902T090000"), 9,
                "19970902T090000,19970902T103000,19970902T120000,19970902T133000");
    }

    public void testEvert20MinutesFrom900AMto440PMEveryDay() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40",
                IcalParseUtil.parseDateValue("19970902T090000"), 48,
                "19970902T090000,19970902T092000,19970902T094000,"
                        + "19970902T100000,19970902T102000,19970902T104000,"
                        + "19970902T110000,19970902T112000,19970902T114000,"
                        + "19970902T120000,19970902T122000,19970902T124000,"
                        + "19970902T130000,19970902T132000,19970902T134000,"
                        + "19970902T140000,19970902T142000,19970902T144000,"
                        + "19970902T150000,19970902T152000,19970902T154000,"
                        + "19970902T160000,19970902T162000,19970902T164000,"
                        + "19970903T090000,19970903T092000,19970903T094000,"
                        + "19970903T100000,19970903T102000,19970903T104000,"
                        + "19970903T110000,19970903T112000,19970903T114000,"
                        + "19970903T120000,19970903T122000,19970903T124000,"
                        + "19970903T130000,19970903T132000,19970903T134000,"
                        + "19970903T140000,19970903T142000,19970903T144000,"
                        + "19970903T150000,19970903T152000,19970903T154000,"
                        + "19970903T160000,19970903T162000,19970903T164000,"
                        + "...");
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16",
                IcalParseUtil.parseDateValue("19970902T090000"), 48,
                "19970902T090000,19970902T092000,19970902T094000,"
                        + "19970902T100000,19970902T102000,19970902T104000,"
                        + "19970902T110000,19970902T112000,19970902T114000,"
                        + "19970902T120000,19970902T122000,19970902T124000,"
                        + "19970902T130000,19970902T132000,19970902T134000,"
                        + "19970902T140000,19970902T142000,19970902T144000,"
                        + "19970902T150000,19970902T152000,19970902T154000,"
                        + "19970902T160000,19970902T162000,19970902T164000,"
                        + "19970903T090000,19970903T092000,19970903T094000,"
                        + "19970903T100000,19970903T102000,19970903T104000,"
                        + "19970903T110000,19970903T112000,19970903T114000,"
                        + "19970903T120000,19970903T122000,19970903T124000,"
                        + "19970903T130000,19970903T132000,19970903T134000,"
                        + "19970903T140000,19970903T142000,19970903T144000,"
                        + "19970903T150000,19970903T152000,19970903T154000,"
                        + "19970903T160000,19970903T162000,19970903T164000,"
                        + "...");
    }

    public void testAnExampleWhereTheDaysGeneratedMakesADifferenceBecauseOfWkst()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=MO",
                IcalParseUtil.parseDateValue("19970805"), 4,
                "19970805,19970810,19970819,19970824");
    }

    public void testAnExampleWhereTheDaysGeneratedMakesADifferenceBecauseOfWkst2()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=SU",
                IcalParseUtil.parseDateValue("19970805"), 8,
                "19970805,19970817,19970819,19970831");
    }

    public void testWithByDayAndByMonthDayFilter() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;COUNT=4;BYDAY=TU,SU;" +
                        "BYMONTHDAY=13,14,15,16,17,18,19,20",
                IcalParseUtil.parseDateValue("19970805"), 8,
                "19970817,19970819,19970914,19970916");
    }

    public void testAnnuallyInAugustOnTuesAndSunBetween13thAnd20th()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;COUNT=4;BYDAY=TU,SU;" +
                        "BYMONTHDAY=13,14,15,16,17,18,19,20;BYMONTH=8",
                IcalParseUtil.parseDateValue("19970605"), 8,
                "19970817,19970819,19980816,19980818");
    }

    public void testLastDayOfTheYearIsASundayOrTuesday()
            throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;COUNT=4;BYDAY=TU,SU;BYYEARDAY=-1",
                IcalParseUtil.parseDateValue("19940605"), 8,
                "19951231,19961231,20001231,20021231");
    }

    public void testLastWeekdayOfMonth() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYSETPOS=-1;BYDAY=-1MO,-1TU,-1WE,-1TH,-1FR",
                IcalParseUtil.parseDateValue("19940605"), 8,
                "19940630,19940729,19940831,19940930,"
                        + "19941031,19941130,19941230,19950131,...");
    }

    public void testMonthsThatStartOrEndOnFriday() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYMONTHDAY=1,-1;BYDAY=FR;COUNT=6",
                IcalParseUtil.parseDateValue("19940605"), 8,
                "19940701,19940930,19950331,19950630,19950901,19951201");
    }

    public void testMonthsThatStartOrEndOnFridayOnEvenWeeks() throws Exception {
        // figure out which of the answers from the above fall on even weeks
        DateValue dtStart = IcalParseUtil.parseDateValue("19940603");
        StringBuilder golden = new StringBuilder();
        for (DateValue candidate : new DateValue[]{
                IcalParseUtil.parseDateValue("19940701"),
                IcalParseUtil.parseDateValue("19940930"),
                IcalParseUtil.parseDateValue("19950331"),
                IcalParseUtil.parseDateValue("19950630"),
                IcalParseUtil.parseDateValue("19950901"),
                IcalParseUtil.parseDateValue("19951201"),
        }) {
            if (0 == TimeUtils.daysBetween(candidate, dtStart) % 14) {
                if (0 != golden.length()) {
                    golden.append(',');
                }
                golden.append(candidate);
            }
        }

        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;BYMONTHDAY=1,-1;BYDAY=FR;COUNT=3",
                dtStart, 8, golden.toString());
    }

    public void testCenturiesThatAreNotLeapYears() throws Exception {
        // I can't think of a good reason anyone would want to specify both a
        // month day and a year day, so here's a really contrived example
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=100;BYYEARDAY=60;BYMONTHDAY=1",
                IcalParseUtil.parseDateValue("19000101"), 4,
                "19000301,21000301,22000301,23000301,...");
    }

    public void testNextCalledWithoutHasNext() throws Exception {
        RecurrenceIterator riter =
                RecurrenceIteratorFactory.createRecurrenceIterator(
                        new RRule("RRULE:FREQ=DAILY"),
                        IcalParseUtil.parseDateValue("20000101"), TimeUtils.utcTimezone());
        assertEquals(IcalParseUtil.parseDateValue("20000101"), riter.next());
        assertEquals(IcalParseUtil.parseDateValue("20000102"), riter.next());
        assertEquals(IcalParseUtil.parseDateValue("20000103"), riter.next());
    }

    public void testNoInstancesGenerated() throws Exception {
        RecurrenceIterator riter =
                RecurrenceIteratorFactory.createRecurrenceIterator(
                        new RRule("RRULE:FREQ=DAILY;UNTIL=19990101"),
                        IcalParseUtil.parseDateValue("20000101"), TimeUtils.utcTimezone());
        assertTrue(!riter.hasNext());

        assertNull(riter.next());
        assertNull(riter.next());
        assertNull(riter.next());
    }

    public void testNoInstancesGenerated2() throws Exception {
        RecurrenceIterator riter =
                RecurrenceIteratorFactory.createRecurrenceIterator(
                        new RRule("RRULE:FREQ=YEARLY;BYMONTH=2;BYMONTHDAY=30"),
                        IcalParseUtil.parseDateValue("20000101"), TimeUtils.utcTimezone());
        assertTrue(!riter.hasNext());
    }

    public void testNoInstancesGenerated3() throws Exception {
        RecurrenceIterator riter =
                RecurrenceIteratorFactory.createRecurrenceIterator(
                        new RRule("RRULE:FREQ=YEARLY;INTERVAL=4;BYYEARDAY=366"),
                        IcalParseUtil.parseDateValue("20010101"), TimeUtils.utcTimezone());
        assertTrue(!riter.hasNext());
    }

    public void testLastWeekdayOfMarch() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYMONTH=3;BYDAY=SA,SU;BYSETPOS=-1",
                IcalParseUtil.parseDateValue("20000101"), 4,
                "20000326,20010331,20020331,20030330,...");
    }

    public void testFirstWeekdayOfMarch() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYMONTH=3;BYDAY=SA,SU;BYSETPOS=1",
                IcalParseUtil.parseDateValue("20000101"), 4,
                "20000304,20010303,20020302,20030301,...");
    }


    //     January 1999
    // Mo Tu We Th Fr Sa Su
    //              1  2  3    // < 4 days, so not a week
    //  4  5  6  7  8  9 10

    //     January 2000
    // Mo Tu We Th Fr Sa Su
    //                 1  2    // < 4 days, so not a week
    //  3  4  5  6  7  8  9

    //     January 2001
    // Mo Tu We Th Fr Sa Su
    //  1  2  3  4  5  6  7
    //  8  9 10 11 12 13 14

    //     January 2002
    // Mo Tu We Th Fr Sa Su
    //     1  2  3  4  5  6
    //  7  8  9 10 11 12 13

    /**
     * Find the first weekday of the first week of the year.
     * The first week of the year may be partial, and the first week is considered
     * to be the first one with at least four days.
     */
    public void testFirstWeekdayOfFirstWeekOfYear() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYWEEKNO=1;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=1",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990104,20000103,20010101,20020101,...");
    }

    public void testFirstSundayOfTheYear1() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYWEEKNO=1;BYDAY=SU",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990110,20000109,20010107,20020106,...");
    }

    public void testFirstSundayOfTheYear2() throws Exception {
        // TODO(msamuel): is this right?
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=1SU",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990103,20000102,20010107,20020106,...");
    }

    public void testFirstSundayOfTheYear3() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=SU;BYYEARDAY=1,2,3,4,5,6,7,8,9,10,11,12,13"
                        + ";BYSETPOS=1",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990103,20000102,20010107,20020106,...");
    }

    public void testFirstWeekdayOfYear() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=1",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990101,20000103,20010101,20020101,...");
    }

    public void testLastWeekdayOfFirstWeekOfYear() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYWEEKNO=1;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-1",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990108,20000107,20010105,20020104,...");
    }

    //     January 1999
    // Mo Tu We Th Fr Sa Su
    //              1  2  3
    //  4  5  6  7  8  9 10
    // 11 12 13 14 15 16 17
    // 18 19 20 21 22 23 24
    // 25 26 27 28 29 30 31

    public void testSecondWeekday1() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=2",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990105,19990112,19990119,19990126,...");
    }

    //     January 1997
    // Mo Tu We Th Fr Sa Su
    //        1  2  3  4  5
    //  6  7  8  9 10 11 12
    // 13 14 15 16 17 18 19
    // 20 21 22 23 24 25 26
    // 27 28 29 30 31

    public void testSecondWeekday2() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=2",
                IcalParseUtil.parseDateValue("19970101"), 4,
                "19970102,19970107,19970114,19970121,...");
    }

    public void testByYearDayAndByDayFilterInteraction() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYYEARDAY=15;BYDAY=3MO",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "20010115,20070115,20180115,20240115,...");
    }

    public void testByDayWithNegWeekNoAsFilter() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;BYMONTHDAY=26;BYDAY=-1FR",
                IcalParseUtil.parseDateValue("19990101"), 4,
                "19990226,19990326,19991126,20000526,...");
    }

    public void testLastWeekOfTheYear() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYWEEKNO=-1",
                IcalParseUtil.parseDateValue("19990101"), 6,
                "19991227,19991228,19991229,19991230,19991231,20001225,...");
    }

    public void testUserSubmittedTest1() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=WE;BYDAY=SU,TU,TH,SA"
                        + ";UNTIL=20000215T113000Z",
                IcalParseUtil.parseDateValue("20000127T033000"), 20,
                "20000127T033000,20000129T033000,20000130T033000,20000201T033000,"
                        + "20000210T033000,20000212T033000,20000213T033000,20000215T033000");
    }

    public void testAdvanceTo() throws Exception {
        // a bunch of tests grabbed from above with an advance-to date tacked on

        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=TH",
                IcalParseUtil.parseDateValue("19970313"), 11,
                /*"19970313,19970320,19970327,"*/"19980305,19980312," +
                        "19980319,19980326,19990304,19990311,19990318," +
                        "19990325,20000302,20000309,20000316,...",
                IcalParseUtil.parseDateValue("19970601"));

        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYDAY=20MO",
                IcalParseUtil.parseDateValue("19970519"), 3,
                /*"19970519,"*/"19980518,19990517,20000515,...",
                IcalParseUtil.parseDateValue("19980515"));

        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=3;UNTIL=20090101;BYYEARDAY=1,100,200",
                IcalParseUtil.parseDateValue("19970101"), 10,
                /*"19970101,19970410,19970719,20000101,"*/"20000409," +
                        "20000718,20030101,20030410,20030719,20060101,20060410,20060719," +
                        "20090101",
                IcalParseUtil.parseDateValue("20000228"));

        // make sure that count preserved
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200",
                IcalParseUtil.parseDateValue("19970101"), 10,
                /*"19970101,19970410,19970719,20000101,"*/"20000409," +
                        "20000718,20030101,20030410,20030719,20060101",
                IcalParseUtil.parseDateValue("20000228"));

        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3",
                IcalParseUtil.parseDateValue("19970310"), 10,
                /*"19970310,"*/"19990110,19990210,19990310,20010110," +
                        "20010210,20010310,20030110,20030210,20030310",
                IcalParseUtil.parseDateValue("19980401"));

        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;UNTIL=19971224",
                IcalParseUtil.parseDateValue("19970902"), 25,
                /*"19970902,19970909,19970916,19970923,"*/"19970930," +
                        "19971007,19971014,19971021,19971028,19971104," +
                        "19971111,19971118,19971125,19971202,19971209," +
                        "19971216,19971223",
                IcalParseUtil.parseDateValue("19970930"));

        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;INTERVAL=18;BYMONTHDAY=10,11,12,13,14,\n" +
                        " 15",
                IcalParseUtil.parseDateValue("19970910"), 5,
        /*"19970910,19970911,19970912,19970913,19970914," +
          "19970915,"*/"19990310,19990311,19990312,19990313,19990314,...",
                IcalParseUtil.parseDateValue("19990101"));

        // advancing into the past
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MONTHLY;INTERVAL=18;BYMONTHDAY=10,11,12,13,14,\n" +
                        " 15",
                IcalParseUtil.parseDateValue("19970910"), 11,
                "19970910,19970911,19970912,19970913,19970914," +
                        "19970915,19990310,19990311,19990312,19990313,19990314,...",
                IcalParseUtil.parseDateValue("19970901"));

        // skips first instance
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=100;BYMONTH=2;BYMONTHDAY=29",
                IcalParseUtil.parseDateValue("19000101"), 5,
                // would return 2000
                "24000229,28000229,32000229,36000229,40000229,...",
                IcalParseUtil.parseDateValue("20040101"));

        // filter hits until date before first instnace
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=100;BYMONTH=2;BYMONTHDAY=29;UNTIL=21000101",
                IcalParseUtil.parseDateValue("19000101"), 5,
                "",
                IcalParseUtil.parseDateValue("20040101"));

        // advancing something that returns no instances
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYMONTH=2;BYMONTHDAY=30",
                IcalParseUtil.parseDateValue("20000101"), 10,
                "",
                IcalParseUtil.parseDateValue("19970901"));

        // advancing something that returns no instances and has a BYSETPOS rule
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYMONTH=2;BYMONTHDAY=30,31;BYSETPOS=1",
                IcalParseUtil.parseDateValue("20000101"), 10,
                "",
                IcalParseUtil.parseDateValue("19970901"));

        // advancing way past year generator timeout
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;BYMONTH=2;BYMONTHDAY=28",
                IcalParseUtil.parseDateValue("20000101"), 10,
                "",
                IcalParseUtil.parseDateValue("25000101"));

        // advancing right to the start
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTHDAY=10;BYMONTH=1;COUNT=3",
                IcalParseUtil.parseDateValue("20100110T140000"), 3,
                "20100110T140000,20110110T140000,20120110T140000");
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTHDAY=10;BYMONTH=1;COUNT=3",
                IcalParseUtil.parseDateValue("20100110T140000"), 3,
                "20100110T140000,20110110T140000,20120110T140000",
                IcalParseUtil.parseDateValue("20100110T140000"));

        // TODO(msamuel): check advancement of more examples
    }

    /** a testcase that yielded dupes due to bysetPos evilness */
    public void testCaseThatYieldedDupes() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;WKST=SU;INTERVAL=1;BYMONTH=9,1,12,8"
                        + ";BYMONTHDAY=-9,-29,24;BYSETPOS=-1,-4,10,-6,-1,-10,-10,-9,-8",
                IcalParseUtil.parseDateValue("20060528"), 200,
                "20060924,20061203,20061224,20070902,20071223,20080803,20080824,"
                        + "20090823,20100103,20100124,20110123,20120902,20121223,20130922,"
                        + "20140803,20140824,20150823,20160103,20160124,20170924,20171203,"
                        + "20171224,20180902,20181223,20190922,20200823,20210103,20210124,"
                        + "20220123,20230924,20231203,20231224,20240922,20250803,20250824,"
                        + "20260823,20270103,20270124,20280123,20280924,20281203,20281224,"
                        + "20290902,20291223,20300922,20310803,20310824,20330123,20340924,"
                        + "20341203,20341224,20350902,20351223,20360803,20360824,20370823,"
                        + "20380103,20380124,20390123,20400902,20401223,20410922,20420803,"
                        + "20420824,20430823,20440103,20440124,20450924,20451203,20451224,"
                        + "20460902,20461223,20470922,20480823,20490103,20490124,20500123,"
                        + "20510924,20511203,20511224,20520922,20530803,20530824,20540823,"
                        + "20550103,20550124,20560123,20560924,20561203,20561224,20570902,"
                        + "20571223,20580922,20590803,20590824,20610123,20620924,20621203,"
                        + "20621224,20630902,20631223,20640803,20640824,20650823,20660103,"
                        + "20660124,20670123,20680902,20681223,20690922,20700803,20700824,"
                        + "20710823,20720103,20720124,20730924,20731203,20731224,20740902,"
                        + "20741223,20750922,20760823,20770103,20770124,20780123,20790924,"
                        + "20791203,20791224,20800922,20810803,20810824,20820823,20830103,"
                        + "20830124,20840123,20840924,20841203,20841224,20850902,20851223,"
                        + "20860922,20870803,20870824,20890123,20900924,20901203,20901224,"
                        + "20910902,20911223,20920803,20920824,20930823,20940103,20940124,"
                        + "20950123,20960902,20961223,20970922,20980803,20980824,20990823,"
                        + "21000103,21000124,21010123,21020924,21021203,21021224,21030902,"
                        + "21031223,21040803,21040824,21050823,21060103,21060124,21070123,"
                        + "21080902,21081223,21090922,21100803,21100824,21110823,21120103,"
                        + "21120124,21130924,21131203,21131224,21140902,21141223,21150922,"
                        + "21160823,21170103,21170124,21180123,21190924,21191203,21191224,"
                        + "21200922,21210803,21210824,21220823,...");
    }

    public void testHourlyWithByday() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=HOURLY;INTERVAL=6;BYDAY=TU,TH;COUNT=5",
                IcalParseUtil.parseDateValue("20110809T123000"), 20,
                "20110809T123000,20110809T183000,20110811T003000,"
                        + "20110811T063000,20110811T123000");
    }

    public void testHourlyWithBydayAcrossMonthBoundary() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=HOURLY;INTERVAL=6;BYDAY=TU,TH;COUNT=5",
                IcalParseUtil.parseDateValue("20110831T123000"), 20,
                "20110901T003000,20110901T063000,20110901T123000,20110901T183000,"
                        + "20110906T003000");
    }

    public void testHourlyWithByMonthday() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=HOURLY;INTERVAL=6;BYMONTHDAY=9;COUNT=5",
                IcalParseUtil.parseDateValue("20110809T123000"), 20,
                "20110809T123000,20110809T183000,20110909T003000,"
                        + "20110909T063000,20110909T123000");
    }

    public void testWeirdByMonth() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTH=2,7,4,9,9,6,11,1",
                IcalParseUtil.parseDateValue("19490320"), 12,
                "19490420,19490620,19490720,19490920,19491120,"
                        + "19500120,19500220,19500420,19500620,19500720,19500920,19501120,...");
    }

    public void testMonkeyByMinute1() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=DAILY;INTERVAL=1;BYMINUTE=19,27,38,1,5",
                IcalParseUtil.parseDateValue("19360508T000941"), 3,
                "19360508T001941,19360508T002741,19360508T003841,...");
    }

    public void testMonkeyByMinute2() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=MINUTELY;WKST=SU;INTERVAL=1;BYMONTH=10,12"
                        + ";BYMONTHDAY=9,28,-5,-19;BYHOUR=13,0,13,8;BYSECOND=51,26,31",
                IcalParseUtil.parseDateValue("19390108T105827"), 9,
                // Since it starts at month 10 instead of January, the minute 58 is
                // irrelevant.
                "19391009T000026,19391009T000031,19391009T000051," +
                        "19391009T000126,19391009T000131,19391009T000151," +
                        "19391009T000226,19391009T000231,19391009T000251,...");
    }

    public void testMonkeyBySecondSetPos() throws Exception {
        runRecurrenceIteratorTest(
                "RRULE:FREQ=WEEKLY;COUNT=13;INTERVAL=1;BYDAY=MO,SA,SU,FR"
                        + ";BYSECOND=6,48,20;BYSETPOS=8,2,5,7,-8,4",
                IcalParseUtil.parseDateValue("19090424T075754"), 9,
                "19090425T075706,19090425T075748,19090430T075706,"
                        + "19090430T075720,19090430T075748,19090501T075706,"
                        + "19090501T075748,19090502T075706,19090507T075706,...");
    }

    public void testMonkeyHourly() throws Exception {
        runRecurrenceIteratorTest(
                "EXRULE:FREQ=HOURLY;INTERVAL=1;BYMONTHDAY=12,10,-4",
                IcalParseUtil.parseDateValue("20510120T031047"), 144,
                "20510128T001047,20510128T011047,20510128T021047,20510128T031047," +
                        "20510128T041047,20510128T051047,20510128T061047,20510128T071047," +
                        "20510128T081047,20510128T091047,20510128T101047,20510128T111047," +
                        "20510128T121047,20510128T131047,20510128T141047,20510128T151047," +
                        "20510128T161047,20510128T171047,20510128T181047,20510128T191047," +
                        "20510128T201047,20510128T211047,20510128T221047,20510128T231047," +
                        "20510210T001047,20510210T011047,20510210T021047,20510210T031047," +
                        "20510210T041047,20510210T051047,20510210T061047,20510210T071047," +
                        "20510210T081047,20510210T091047,20510210T101047,20510210T111047," +
                        "20510210T121047,20510210T131047,20510210T141047,20510210T151047," +
                        "20510210T161047,20510210T171047,20510210T181047,20510210T191047," +
                        "20510210T201047,20510210T211047,20510210T221047,20510210T231047," +
                        "20510212T001047,20510212T011047,20510212T021047,20510212T031047," +
                        "20510212T041047,20510212T051047,20510212T061047,20510212T071047," +
                        "20510212T081047,20510212T091047,20510212T101047,20510212T111047," +
                        "20510212T121047,20510212T131047,20510212T141047,20510212T151047," +
                        "20510212T161047,20510212T171047,20510212T181047,20510212T191047," +
                        "20510212T201047,20510212T211047,20510212T221047,20510212T231047," +
                        "20510225T001047,20510225T011047,20510225T021047,20510225T031047," +
                        "20510225T041047,20510225T051047,20510225T061047,20510225T071047," +
                        "20510225T081047,20510225T091047,20510225T101047,20510225T111047," +
                        "20510225T121047,20510225T131047,20510225T141047,20510225T151047," +
                        "20510225T161047,20510225T171047,20510225T181047,20510225T191047," +
                        "20510225T201047,20510225T211047,20510225T221047,20510225T231047," +
                        "20510310T001047,20510310T011047,20510310T021047,20510310T031047," +
                        "20510310T041047,20510310T051047,20510310T061047,20510310T071047," +
                        "20510310T081047,20510310T091047,20510310T101047,20510310T111047," +
                        "20510310T121047,20510310T131047,20510310T141047,20510310T151047," +
                        "20510310T161047,20510310T171047,20510310T181047,20510310T191047," +
                        "20510310T201047,20510310T211047,20510310T221047,20510310T231047," +
                        "20510312T001047,20510312T011047,20510312T021047,20510312T031047," +
                        "20510312T041047,20510312T051047,20510312T061047,20510312T071047," +
                        "20510312T081047,20510312T091047,20510312T101047,20510312T111047," +
                        "20510312T121047,20510312T131047,20510312T141047,20510312T151047," +
                        "20510312T161047,20510312T171047,20510312T181047,20510312T191047," +
                        "20510312T201047,20510312T211047,20510312T221047,20510312T231047," +
                        "..."
        );
    }

    // TODO(msamuel): test BYSETPOS with FREQ in (WEEKLY,MONTHLY,YEARLY) x
    // (setPos absolute, setPos relative, setPos mixed)

    // TODO(msamuel): test that until date properly compared to UTC dates.

    // TODO(msamuel): test that monotonically increasing over timezone boundaries

    // TODO(msamuel): test that advanceTo handles timezones properly

}
