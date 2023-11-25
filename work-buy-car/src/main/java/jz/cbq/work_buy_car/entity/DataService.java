package jz.cbq.work_buy_car.entity;



import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_buy_car.R;

/**
 * 源数据提供者
 */
public class DataService {
    /**
     * 根据 position 获取 list
     *
     * @param position position
     * @return list
     */
    public static List<ProductInfo> getListData(int position) {
        List<ProductInfo> list = new ArrayList<>();
        if (position == 0) {
            list.add(new ProductInfo(1, R.drawable.p_0_1, "夏季短袖T恤", "舒适透气的夏季短袖T恤，适合日常休闲穿着。", 39));
            list.add(new ProductInfo(2, R.drawable.p_0_2, "牛仔裤", "经典款牛仔裤，适合各种场合穿着。", 119));
            list.add(new ProductInfo(3, R.drawable.p_0_3, "连衣裙", "时尚简约的连衣裙，适合派对和约会穿着。", 89));
            list.add(new ProductInfo(4, R.drawable.p_0_4, "运动裤", "舒适透气的运动裤，适合运动和健身穿着。", 79));
            list.add(new ProductInfo(5, R.drawable.p_0_5, "风衣", "时尚简约的风衣，适合春秋季节穿着。", 189));
        } else if (position == 1){
            list.add(new ProductInfo(101, R.drawable.p_1_1, "智能手机", "高性能智能手机，配置强大，拍照效果出色。", 1999));
            list.add(new ProductInfo(102, R.drawable.p_1_2, "平板电脑", "轻薄便携的平板电脑，适合娱乐和办公使用。", 1299));
            list.add(new ProductInfo(103, R.drawable.p_1_3, "耳机", "高音质蓝牙耳机，舒适佩戴，适合运动和旅行。", 188));
            list.add(new ProductInfo(104, R.drawable.p_1_4, "智能手表", "功能强大的智能手表，支持健康监测和智能通知。", 599));
        }else if (position == 2){
            list.add(new ProductInfo(1001, R.drawable.p_2_1, "保温杯", "不锈钢保温杯，保温效果好，容量适中。", 59));
            list.add(new ProductInfo(1002, R.drawable.p_2_2, "电风扇", "静音电风扇，风力强劲，适合夏季使用。", 139));
            list.add(new ProductInfo(1003, R.drawable.p_2_3, "洗衣液", "高效去污的洗衣液，衣物清洁彻底。", 29));
        }else if (position == 3){
            list.add(new ProductInfo(10001, R.drawable.p_3_1, "经典硬皮笔记本", "经典款式的硬皮笔记本，尺寸适中，方便携带。", 15));
            list.add(new ProductInfo(10002, R.drawable.p_3_2, "文具套装", "包含铅笔、橡皮、尺子等文具的套装。", 19));
            list.add(new ProductInfo(10003, R.drawable.p_3_3, "书包", "时尚耐用的书包，适合学生使用。", 99));
        }
        return list;
    }
}
