package classes;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import exceptions.OutOfRange;
import exceptions.WrongExtension;
import exceptions.WrongTimeFormat;
import org.w3c.dom.ls.LSOutput;

import java.util.Objects;

public class Time {
    private int hour;
    private int minutes;

    public Time(){hour=minutes=0;}

    public Time(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
        roundHour();
    }

    public Time(Time time) {
        this.hour=time.getHour();
        this.minutes=time.getMinutes();
        roundHour();
    }

    @Override
    public String toString() {
        String format=String.format("%02d",hour);
        String format2=String.format("%02d",minutes);
        return
                format + ":" + format2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return hour == time.hour &&
                minutes == time.minutes;
    }

    public int compareTime(Time time) {
        if (this.hour > time.getHour()) {
            return 1;
        }
        else if (this.hour == time.getHour()) {
            if (this.minutes > time.getMinutes()) {
                return 1;
            } else if (this.minutes == time.getMinutes()) {
                return 0;
            } else
                return -1;
        }
        else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, minutes);
    }

    public void updateTime(int hour, int minutes) {
        if (this.minutes + minutes > 59) {
            this.hour++;
        }
        this.hour = Math.floorMod((this.hour + hour) , 24);
        if(this.minutes + minutes<0) {
            this.hour--;
        }
        this.minutes = Math.floorMod((this.minutes + minutes),60);
        this.roundHour();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void roundHour() {
        if(minutes>57) {
           // hour=(hour++)%24;
            hour++;
            hour=hour%24;
            minutes=0;
        }
        if(minutes%5<3)
            minutes=minutes-minutes%5;
        else
            minutes=minutes+(5-minutes%5);
    }

    public boolean isBetween(Time begin,Time end,boolean isDeparture){
        if(getHour()>=begin.getHour()&&getHour()<=end.getHour()){

            if((this.equals(end)&&isDeparture)||(this.equals(begin)&&!isDeparture))
                return false;


            if(begin.getHour()==end.getHour()) {
                if (getMinutes() >= begin.getMinutes() && getMinutes() <= end.getMinutes()) {
                    return true;
                } else {
                    return false;
                }
            }

            if(getHour()==begin.getHour()){
                if(getMinutes()>=begin.getMinutes()){
                    return true;
                }
                else{
                    return false;
                }
            }

            if(getHour()==end.getHour()){
                if(getMinutes()<=end.getMinutes()){
                    return true;
                }
                else{
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
