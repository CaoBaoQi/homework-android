package jz.cbq.work_note_book.entity;

/**
 * 笔记实体类
 *
 * @author cbq
 * @date 2023/11/20 22:29
 * @since 1.0.0
 */
public class Note {
    /**
     * 笔记 id
     */
    private Integer _id;
    /**
     * 笔记标题
     */
    private String title;
    /**
     * 笔记内容
     */
    private String content;
    /**
     * 创建日期
     */
    private String date_created;
    /**
     * 更新日期
     */
    private String date_updated;
    /**
     * 笔记是否处于回收状态
     * 0 未回收 | 1 已回收
     */
    private int recycle_status;

    public Note() {
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Note(Integer _id, String title, String content) {
        this._id = _id;
        this.title = title;
        this.content = content;
    }

    public Note(String title, String content, String date_created, String date_updated) {
        this.title = title;
        this.content = content;
        this.date_created = date_created;
        this.date_updated = date_updated;
    }

    public Note(Integer _id, String title, String content, String date_created, String date_updated) {
        this._id = _id;
        this.title = title;
        this.content = content;
        this.date_created = date_created;
        this.date_updated = date_updated;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public int getRecycle_status() {
        return recycle_status;
    }

    public void setRecycle_status(int recycle_status) {
        this.recycle_status = recycle_status;
    }
}
