package jz.cbq.work_buy_car.entity;


import java.io.Serializable;

/**
 * 商品信息
 */
public class ProductInfo implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * img 图片
     */
    private int img;
    /**
     * title 标题
     */
    private String title;
    /**
     * description 描述
     */
    private String description;
    /**
     * price 价格
     */
    private int price;

    public ProductInfo() {
    }

    public ProductInfo(int id, int img, String title, String description, int price) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
