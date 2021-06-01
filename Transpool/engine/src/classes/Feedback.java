package classes;

public class Feedback {
    private int rate;
    private String feedback;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Feedback(int rate, String feedback) {
        this.rate = rate;
        this.feedback = feedback;
    }
}
