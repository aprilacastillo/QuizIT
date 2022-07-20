package levelapp;

public class LeaderboardModel implements Comparable{
    String name,score;
    public LeaderboardModel() {
    }

    public LeaderboardModel(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        String compareTo=((LeaderboardModel )o).getScore();
        int convert = Integer.parseInt(compareTo);
        /* For Ascending order*/
        return convert-Integer.parseInt(this.score);

        /* For Descending order do like this */
        //return compareage-this.rating;
    }
}
