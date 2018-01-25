package equity.com.fourgr.server.model;

public class Submission {
    private String submission_date, firstname, lastname, town, camp, other_comments,spinner1;
    private double latitude, longitude;

    public Submission(String submission_date, String firstname, String lastname, String town, String other_comments, String camp, String spinner1) {
        this.submission_date = submission_date;
        this.firstname = firstname;
        this.lastname = lastname;
        this.town = town;
        this.other_comments = other_comments;
        this.camp = camp;
        this.spinner1 = spinner1;
        //this.latitude = latitude;
       // this.longitude = longitude;


    }

    public Submission(){

    }

    public String getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(String submission_date) {
        this.submission_date = submission_date;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String media_owner) {
        this.lastname = lastname;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getOther_comments() {
        return other_comments;
    }

    public void setOther_comments(String other_comments) {
        this.other_comments = other_comments;
    }

    public String getCamp() {
        return camp;
    }

    public void setCamp(String camp) {
        this.camp = camp;
    }

    public String getSpinner1() {
        return spinner1;
    }

    public void setSpinner1(String spinner1 ) {
        this.spinner1 = spinner1;
    }

   /*

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }*/



}
