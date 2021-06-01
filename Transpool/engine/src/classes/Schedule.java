package classes;

public class Schedule {
    private boolean oneTime;
    private boolean daily;
    private boolean alternatly;
    private boolean weekly;
    private boolean monthly;


    public Schedule(String schedule) {
        if(schedule.equals("One time"))
            oneTime=true;
        else if(schedule.equals("Daily"))
            daily=true;
        else if(schedule.equals("Alternatly")||schedule.equals("BiDaily"))
            alternatly=true;
        else if(schedule.equals("Weekly"))
            weekly=true;
        else if(schedule.equals("Monthly"))
            monthly=true;
    }

    public String getSchedule() {
        if(oneTime==true)
            return "One time";
        else if(daily==true)
            return "Daily";
        else if(alternatly==true)
            return "Alternatly";
        else if(weekly==true)
            return "Weekly";
        else if(monthly==true)
            return "Monthly";
        return "There is no schedule to the trip";
    }

    public int recurrencesToInteger() {
        if(oneTime==true)
            return Integer.MAX_VALUE;
        else if(daily==true)
            return 1;
        else if(alternatly==true)
            return 2;
        else if(weekly==true)
            return 7;
        else if(monthly==true)
            return 30;
        return 1;
    }
}
