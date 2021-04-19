package com.yxkong.agent.dto.enums;


public enum ResultStatusEnum {

    /**
     * 成功状态1
     */
    SUCCESS("1", "执行成功！"),
    /**
     * 失败状态0
     */
    ERROR("0", "执行失败！"),
    /**
     * 没有查到数据
     */
    NO_DATA("1000", "没有查到数据"),
    /**
     * 必填项参数为空
     */
    PARAM_EMPTY("1001", "必填项参数为空"),
    /**
     * 参数异常
     */
    PARAM_ERROR("1002", "参数异常"),
    /**
     * 来源错误
     */
    SOURCE_ERROR("1003", "来源错误"),
    /**
     * 无权限
     */
    NO_AUTH("1004", "无权限"),
    /**
     * 验签失败
     */
    SIGN("1005", "验签失败"),
    /**
     * 重复提交
     */
    REPEAT("1006", "重复提交"),
    /**
     * 非法请求
     */
    BAD_REQUEST("1007", "非法请求"),
    /**
     * toen无效，请重新登录
     */
    TOKEN_INVALID("1008", "toen无效，请重新登录"),
    /**
     * 请求的时间戳_t超过30分钟
     */
    TIME_INVALID("1009", "请求的时间戳_t超过30分钟"),
    /**
     * 已禁用
     */
    CODE_OFF("1010", "已禁用"),
    /**
     * 未登录
     */
    NO_LOGIN("1011", "未登录"),
    /**
     * 参数值不在处理的范围内
     */
    UNRECOGNIZED("1012", "参数值不在处理的范围内"),
    /**
     * 数据校验失败
     */
    CHECKEXCEPTION("1013", "数据校验失败");

    private String status;

    private String message;

    private ResultStatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
