package cn.com.yizhu.contants;

/**
 * 文件状态枚举，0：正常，1：删除
 */
public enum FileStatusEnum {

    NORMAL(0, "正常"),
    DELETE(1, "删除");

    FileStatusEnum(int value, String desc){
        this.value = value;
        this.desc = desc;
    }

    private int value;

    private String desc;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
