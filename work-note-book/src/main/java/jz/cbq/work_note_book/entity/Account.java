package jz.cbq.work_note_book.entity;

/**
 * 账户
 *
 * @author cbq
 * @date 2023/11/25 16:23
 * @since 1.0.0
 */
public class Account {
    /**
     * id
     */
    private Integer _id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;

    /**
     * 已登录账户信息
     */
    public static Account loginedAccount;

    public Account() {
    }

    public Account(Integer _id, String username, String email, String password) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public static Account getLoginedAccount() {
        return loginedAccount;
    }

    public static void setLoginedAccount(Account loginedAccount) {
        Account.loginedAccount = loginedAccount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "_id=" + _id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
