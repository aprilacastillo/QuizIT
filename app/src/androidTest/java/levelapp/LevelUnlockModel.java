package levelapp;

public class LevelUnlockModel {

    String databaseHard, databaseMed,imMed, imHard,mobHard, mobMed,netHard, netMed,webHard, webMed;

    public LevelUnlockModel(){

    }

    public LevelUnlockModel(String databaseHard, String databaseMed, String imMed, String imHard, String mobHard, String mobMed, String netHard, String netMed, String webHard, String webMed) {
        this.databaseHard = databaseHard;
        this.databaseMed = databaseMed;
        this.imMed = imMed;
        this.imHard = imHard;
        this.mobHard = mobHard;
        this.mobMed = mobMed;
        this.netHard = netHard;
        this.netMed = netMed;
        this.webHard = webHard;
        this.webMed = webMed;
    }

    public String getDatabaseHard() {
        return databaseHard;
    }

    public void setDatabaseHard(String databaseHard) {
        this.databaseHard = databaseHard;
    }

    public String getDatabaseMed() {
        return databaseMed;
    }

    public void setDatabaseMed(String databaseMed) {
        this.databaseMed = databaseMed;
    }

    public String getImMed() {
        return imMed;
    }

    public void setImMed(String imMed) {
        this.imMed = imMed;
    }

    public String getImHard() {
        return imHard;
    }

    public void setImHard(String imHard) {
        this.imHard = imHard;
    }

    public String getMobHard() {
        return mobHard;
    }

    public void setMobHard(String mobHard) {
        this.mobHard = mobHard;
    }

    public String getMobMed() {
        return mobMed;
    }

    public void setMobMed(String mobMed) {
        this.mobMed = mobMed;
    }

    public String getNetHard() {
        return netHard;
    }

    public void setNetHard(String netHard) {
        this.netHard = netHard;
    }

    public String getNetMed() {
        return netMed;
    }

    public void setNetMed(String netMed) {
        this.netMed = netMed;
    }

    public String getWebHard() {
        return webHard;
    }

    public void setWebHard(String webHard) {
        this.webHard = webHard;
    }

    public String getWebMed() {
        return webMed;
    }

    public void setWebMed(String webMed) {
        this.webMed = webMed;
    }
}
