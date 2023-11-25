package jz.cbq.work_buy_car.entity;


/**
 * 购物车信息
 */

public class CarInfo {
    /**
     * id
     */
    private Integer car_id;
    /**
     * username
     */
    private String username;
    /**
     * 商品 id
     */
    private int product_id;
    /**
     * 商品 img
     */
    private int product_img;
    /**
     * 商品 title
     */
    private String product_title;
    /**
     * 商品 description
     */
    private String product_description;
    /**
     * 商品 price
     */
    private int product_price;

    /**
     * 商品 count
     */
    private int product_count;

    public CarInfo() {
    }

    public CarInfo(Integer car_id, String username, int product_id, int product_img, String product_title, String product_description, int product_price, int product_count) {
        this.car_id = car_id;
        this.username = username;
        this.product_id = product_id;
        this.product_img = product_img;
        this.product_title = product_title;
        this.product_description = product_description;
        this.product_price = product_price;
        this.product_count = product_count;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_img() {
        return product_img;
    }

    public void setProduct_img(int product_img) {
        this.product_img = product_img;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }
}

