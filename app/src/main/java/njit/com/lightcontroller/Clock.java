package njit.com.lightcontroller;

import java.util.Calendar;

/**
 * Created by lenovo on 2019/5/1.
 */

import java.util.Calendar;

/**
 * Created by zdd on 16-7-23.
 */
public class Clock {

    private int whichClock=1;
    private String command="time";
    private String time;
    private String clockStatus="ON";
    private String repeatTimes="仅一次";
    private int repeatTimes_int=0;
    private String clockChangeWay="new";//modify和new两种值

    public int getWhichClock() {
        return whichClock;
    }

    public Clock(Calendar time) {
        this.time = TimeTool.turnDateToString(time.getTime());
    }

    public Clock(){
        this(Calendar.getInstance());
    }

    public String getClockChangeWay() {
        return clockChangeWay;
    }

    public int getRepeatTimes_int() {
        return repeatTimes_int;
    }

    public String getTime() {
        return time;
    }

    public String getCommand() {
        return command;
    }

    public String getClockTime() {
        return time;
    }

    public String getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(String repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public String getClockStatus() {
        return clockStatus;
    }


    public void setClockChangeWay(String clockChangeWay) {
        this.clockChangeWay = clockChangeWay;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setRepeatTimes_int(int repeatTimes_int) {
        this.repeatTimes_int = repeatTimes_int;
    }

    public void setWhichClock(int whichAlarm) {
        this.whichClock = whichAlarm;
    }

    public void setClockStatus(String clockStatus) {
        this.clockStatus = clockStatus;
    }
}
