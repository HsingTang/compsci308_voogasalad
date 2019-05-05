package data.external;

/**
 * Packages a score for a user on a given game in a way that is easily passed between data and center
 */
public class UserScore implements Comparable<UserScore> {

    private String myUserName;
    private Double myScore;

    /**
     * UserScore constructor
     * @param userName user name
     * @param score score
     */
    public UserScore(String userName, Double score) {
        myUserName = userName;
        myScore = score;
    }

    /**
     * Getter for the user name
     * @return user name
     */
    public String getUserName() {
        return myUserName;
    }

    /**
     * Getter for the score
     * @return score
     */
    public Double getScore() {
        return myScore;
    }

    /**
     * Allows for sorting from high to low
     * @param o other object to compare to
     * @return -1 if my score is higher than other score
     */
    @Override
    public int compareTo(UserScore o) {
        return -myScore.compareTo(o.myScore);
    }

    /**
     * Equals method just used for testing
     * @param obj object to compare to
     * @return true if all instance variables match
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            UserScore otherScore = (UserScore) obj;
            return myUserName.equals(otherScore.myUserName) && myScore.equals(otherScore.myScore);
        }
        return false;
    }
}
