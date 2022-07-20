package levelapp;

public class ScoreModel {
    String sDatabaseEasy, sDatabaseHard, sDatabaseMed, sImEasy,sImMed, sImHard, sMobEasy, sMobHard, sMobMed, sNetEasy, sNetHard, sNetMed, sWebEasy, sWebHard, sWebMed;

    public ScoreModel(){
        //sfe
    }

    public ScoreModel(String DatabaseEasy, String DatabaseHard, String DatabaseMed, String ImEasy,String ImMed, String ImHard, String MobEasy, String MobHard, String MobMed, String NetEasy, String NetHard, String NetMed, String WebEasy, String WebHard, String WebMed) {
        sDatabaseEasy = DatabaseEasy;
        sDatabaseHard = DatabaseHard;
        sDatabaseMed = DatabaseMed;
        sImEasy = ImEasy;
        sImMed = ImMed;
        sImHard = ImHard;
        sMobEasy = MobEasy;
        sMobHard = MobHard;
        sMobMed = MobMed;
        sNetEasy = NetEasy;
        sNetHard = NetHard;
        sNetMed = NetMed;
        sWebEasy = WebEasy;
        sWebHard = WebHard;
        sWebMed = WebMed;
    }

    public String getDatabaseEasy() {
        return sDatabaseEasy;
    }

    public void setDatabaseEasy(String DatabaseEasy) {
        sDatabaseEasy = DatabaseEasy;
    }

    public String getDatabaseHard() {
        return sDatabaseHard;
    }

    public void setDatabaseHard(String DatabaseHard) {
        sDatabaseHard = DatabaseHard;
    }

    public String getDatabaseMed() {
        return sDatabaseMed;
    }

    public void setDatabaseMed(String DatabaseMed) {
        sDatabaseMed = DatabaseMed;
    }

    public String getImEasy() {
        return sImEasy;
    }

    public void setImEasy(String ImEasy) {
        sImEasy = ImEasy;
    }

    public String getImHard() {
        return sImHard;
    }

    public void setImHard(String ImHard) {
        sImHard = ImHard;
    }

    public String getMobEasy() {
        return sMobEasy;
    }

    public void setMobEasy(String MobEasy) {
        sMobEasy = MobEasy;
    }

    public String getMobHard() {
        return sMobHard;
    }

    public void setMobHard(String MobHard) {
        sMobHard = MobHard;
    }

    public String getMobMed() {
        return sMobMed;
    }

    public void setMobMed(String MobMed) {
        sMobMed = MobMed;
    }

    public String getNetEasy() {
        return sNetEasy;
    }

    public void setNetEasy(String NetEasy) {
        sNetEasy = NetEasy;
    }

    public String getNetHard() {
        return sNetHard;
    }

    public void setNetHard(String NetHard) {
        sNetHard = NetHard;
    }

    public String getNetMed() {
        return sNetMed;
    }

    public void setNetMed(String NetMed) {
        sNetMed = NetMed;
    }

    public String getWebEasy() {
        return sWebEasy;
    }

    public void setWebEasy(String WebEasy) {
        sWebEasy = WebEasy;
    }

    public String getWebHard() {
        return sWebHard;
    }

    public void setWebHard(String WebHard) {
        sWebHard = WebHard;
    }

    public String getWebMed() {
        return sWebMed;
    }

    public void setWebMed(String WebMed) {
        sWebMed = WebMed;
    }


    public String getImMed() {
        return sImMed;
    }

    public void setImMed(String ImMed) {
        sImMed = ImMed;
    }
}
