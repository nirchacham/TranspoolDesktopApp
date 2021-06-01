package classes;

import java.util.ArrayList;
import java.util.List;

public class Feedbacks {
    private List<Feedback> feedbacks;
    private double averageRate;
    private int numOfFeedbacks;

    public Feedbacks() {
        this.feedbacks = new ArrayList<>();
        this.averageRate = 0;
        this.numOfFeedbacks = 0;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public int getNumOfFeedbacks() {
        return numOfFeedbacks;
    }


    public void addFeedback(Feedback feedback){
        feedbacks.add(feedback);
        numOfFeedbacks++;
        averageRate = (feedback.getRate()+averageRate)/numOfFeedbacks;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }


}
