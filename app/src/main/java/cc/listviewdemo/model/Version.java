package cc.listviewdemo.model;

/**
 * 作者：lwj on 2016/7/16 09:53
 * 邮箱：1031066280@qq.com
 */
public class Version {

    /**
     * version : 1.0
     * time : 2016-07-18
     * content : 升级内容
     * download : http://www.dakedaojia.com/download/android.apk
     */

    private String version;
    private String time;
    private String content;
    private String download;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
}
