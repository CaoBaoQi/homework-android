package jz.cbq.work_buy_car.entity;



/**
 * 订单信息
 */

public class OrderInfo {
    /**
     * id
     */
    private Integer order_id;
    /**
     * username
     */
    private String username;
    /**
     * 商品 img
     */
    private int product_img;
    /**
     * 商品 title
     */
    private String product_title;

    /**
     * 商品 price
     */
    private int product_price;

    /**
     * 商品 count
     */
    private int product_count;
    /**
     * 地址
     */
    private String address;
    /**
     * 电话
     */
    private String mobile;

    public OrderInfo() {
    }

    public OrderInfo(Integer order_id, String username, int product_img, String product_title, int product_price, int product_count, String address, String mobile) {
        this.order_id = order_id;
        this.username = username;
        this.product_img = product_img;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_count = product_count;
        this.address = address;
        this.mobile = mobile;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

