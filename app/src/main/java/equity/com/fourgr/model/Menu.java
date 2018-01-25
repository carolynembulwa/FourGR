package equity.com.fourgr.model;

public class Menu {
    private int _id, _menu_icon;
    private String _menu_title, _menu_slug, _menu_users;

    public int get_id() {
        return _id;
    }

    public String get_menu_title() {
        return _menu_title;
    }

    public void set_menu_title(String _menu_title) {
        this._menu_title = _menu_title;
    }

    public String get_menu_slug() {
        return _menu_slug;
    }

    public void set_menu_slug(String _menu_slug) {
        this._menu_slug = _menu_slug;
    }

    public String get_menu_users() {
        return _menu_users;
    }

    public void set_menu_users(String _menu_users) {
        this._menu_users = _menu_users;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_menu_icon() {
        return _menu_icon;
    }

    public void set_menu_icon(int _menu_icon) {
        this._menu_icon = _menu_icon;
    }
}
