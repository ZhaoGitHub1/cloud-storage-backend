package cn.com.yizhu.common.vo;

/**
 * 全局响应VO
 */
public class ResponseVO<T> {

    private Boolean success;

    private T data;

    private String message;

    public static final String SUCCESS__MEG = "请求成功！";
    public static final String FAIL_MSG = "请求失败！";

    public ResponseVO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseVO(Boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static ResponseVO successResponseVO(){
        return new ResponseVO(true, SUCCESS__MEG);
    }

    public static ResponseVO successResponseVO(Object data){
        return new ResponseVO(true, data, SUCCESS__MEG);
    }

    public static ResponseVO fileResponseVO(String message){
        return new ResponseVO(false, message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
