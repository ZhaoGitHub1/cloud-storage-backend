package cn.com.yizhu.contants;

/**
 * 文件类型枚举，0：文件，1：文件夹
 */
public enum FileTypeEnum {

    FILE(0, "文件"),
    DIR(1, "文件夹");

    FileTypeEnum(int value, String desc){
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
