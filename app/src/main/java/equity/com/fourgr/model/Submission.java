package equity.com.fourgr.model;

        import android.annotation.SuppressLint;
        import android.os.Parcel;
        import android.os.Parcelable;


@SuppressLint("ParcelCreator")
public class Submission implements Parcelable {

    private int _id, _status;
    private String  _town, _spinner1,  _submission_date,_other_comments, _created_at, _firstname, _lastname, _photo_1, _photo_2, _refugee_id,_camp, _latitude, _longitude;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_town() {
        return _town;
    }

    public void set_town(String _town) {
        this._town = _town;
    }


    public String get_refugee_id() {
        return _refugee_id;
    }

    public void set_refugee_id(String _refugee_id) {
        this._refugee_id = _refugee_id;
    }


    public String get_camp() {
        return _camp;
    }

    public void set_camp(String _camp) {
        this._camp = _camp;
    }

    public String get_latitude() {
        return _latitude;
    }

    public void set_latitude(String _latitude) {
        this._latitude = _latitude;
    }

    public String get_longitude() {
        return _longitude;
    }

    public void set_longitude(String _longitude) {
        this._longitude = _longitude;
    }


    public String get_submission_date() {
        return _submission_date;
    }

    public void set_submission_date(String _submission_date) {
        this._submission_date = _submission_date;
    }

    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String _created_at) {
        this._created_at = _created_at;
    }



    public String get_firstname() {
        return _firstname;
    }

    public void set_firstname(String _firstname) {
        this._firstname = _firstname;
    }

    public String get_lastname() {
        return _lastname;
    }

    public void set_lastname(String _lastname) {
        this._lastname = _lastname;
    }

    public String get_spinner1() {
        return _spinner1;
    }

    public void set_spinner1(String _spinner1) {
        this._spinner1 = _spinner1;
    }



    public String get_other_comments() {
        return _other_comments;
    }

    public void set_other_comments(String _other_comments) {
        this._other_comments = _other_comments;
    }

    public String get_photo_1() {
        return _photo_1;
    }

    public void set_photo_1(String _photo_1) {
        this._photo_1 = _photo_1;
    }

    public String get_photo_2() {
        return _photo_2;
    }

    public void set_photo_2(String _photo_2) {
        this._photo_2 = _photo_2;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public int get_status() {
        return _status;
    }

    public void set_status(int _status) {
        this._status = _status;
    }
}
