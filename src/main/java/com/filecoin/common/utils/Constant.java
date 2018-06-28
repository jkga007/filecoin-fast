package com.filecoin.common.utils;

/**
 * 常量
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

	/** 验证码短信模版 **/
	public static final String IDENTIFYING_CODE_SMS_TEMPLATE_CODE = "SMS_138315045";

	/**
	 * 菜单类型
	 * 
	 * @author r25437,g20416
	 * @email support@filecoinon.com
	 * @date 2016年11月15日 下午1:24:29
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author r25437,g20416
     * @email support@filecoinon.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 用户状态
     */
    public enum UserStatus {
        /**
         * 正常
         */
        OK(1),
        /**
         * 需要激活
         */
        NEED_ACTIVE(2),

        /**
         * 需要绑定手机号
         */
        NEED_BIND_MOBILE(3),

        /**
         * 需要补充矿工资料
         */
        NEED_INPUT_MINER(4),
        /**
         * 锁定
         */
        CLOCK(0);

        private int value;

        UserStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 发送状态
     */
    public enum SendStatus {
        /**
         * 已发送
         */
        NEED_SEND(0),
        /**
         * 发送成功
         */
        SUCCESS(1),
        /**
         * 发送失败
         */
        ERROR(2);

        private int value;

        SendStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
