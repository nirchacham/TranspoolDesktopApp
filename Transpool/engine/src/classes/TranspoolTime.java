package classes;

public class TranspoolTime {
    private int dayNumber;
    //private static int dayCounter = 0;

    private Time appTime;
   // private static int timeCounter = 0;


    public TranspoolTime(){
        appTime=new Time();
        dayNumber=1;
    }

    public void updateTime(int hour, int minutes, boolean isForward){
        int prevHour=appTime.getHour();
        int prevMinutes=appTime.getMinutes();
        if(isForward){
            if(hour==24) {// user pressed on day
                dayNumber++;
                return;
            }


            appTime.updateTime(hour,minutes);
            if(appTime.getHour()<prevHour)
                dayNumber++;
        }
        else{
            if(hour==24) {// user pressed on day
                if(dayNumber==1) {
                    //display error
                    return;
                }
                dayNumber--;
                return;
            }
            appTime.updateTime(hour*-1,minutes*-1);
            if(appTime.getHour()>prevHour)
                dayNumber--;
            if(dayNumber==0){
                //display error
                dayNumber++;
                appTime.setHour(prevHour);
                appTime.setMinutes(prevMinutes);
            }
        }
    }


    public int getDayNumber() {
        return dayNumber;
    }

    public Time getAppTime() {
        return appTime;
    }


}
