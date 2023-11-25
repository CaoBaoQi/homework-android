package jz.cbq.work_buy_car.entity;


/**
 * 用户信息
 */

public class UserInfo {
    /**
     * id
     */
    private Integer user_id;
    /**
     * username
     */
    private String username;
    /**
     * nickname
     */
    private String nickname;
    /**
     * email
     */
    private String email;
    /**
     * password
     */
    private String password;

    /**
     * 当前登录用户信息
     */
    public static UserInfo CurrentUserInfo;


    public UserInfo() {
    }

    public UserInfo(Integer user_id, String username, String nickname, String email, String password) {
        this.user_id = user_id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserInfo getCurrentUserInfo() {
        return CurrentUserInfo;
    }

    public static void setCurrentUserInfo(UserInfo currentUserInfo) {
        CurrentUserInfo = currentUserInfo;
    }
}

