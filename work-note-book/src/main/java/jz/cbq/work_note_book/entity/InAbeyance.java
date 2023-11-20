package jz.cbq.work_note_book.entity;

/**
 * 待办实体类
 *
 * @author cbq
 * @date 2023/11/20 22:33
 * @since 1.0.0
 */
public class InAbeyance {
    /**
     * 待办 id
     */
    private int _id;
    /**
     * 待办内容
     */
    private String content;
    /**
     * 待办 提醒日期
     */
    private String date_remind;
    /**
     * 待办 创建日期
     */
    private String date_created;
    /**
     * 待办 完成状态
     */
    private int status;

    public InAbeyance() {
    }

    public InAbeyance(int _id, String content, String date_remind, String date_created, int status) {
        this._id = _id;
        this.content = content;
        this.date_remind = date_remind;
        this.date_created = date_created;
        this.status = status;
    }

    public InAbeyance(int _id, String content, String date_remind) {
        this._id = _id;
        this.content = content;
        this.date_remind = date_remind;
    }

    public InAbeyance(String content, String date_remind, String date_created) {
        this.content = content;
        this.date_remind = date_remind;
        this.date_created = date_created;
    }

    public InAbeyance(int _id, String content, String date_remind, int status) {
        this._id = _id;
        this.content = content;
        this.date_remind = date_remind;
        this.status = status;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_remind() {
        return date_remind;
    }

    public void setDate_remind(String date_remind) {
        this.date_remind = date_remind;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InAbeyance{" +
                "_id=" + _id +
                ", content='" + content + '\'' +
                ", date_remind='" + date_remind + '\'' +
                ", date_created='" + date_created + '\'' +
                ", status=" + status +
                '}';
    }
}
