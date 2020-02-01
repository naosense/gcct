package io.github.pingao777.common.model;

import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/7.
 */
public class JdkInfo extends ResultData {
    private String jvm;
    private String jvmVersion;
    private String jdkVersion;

    public JdkInfo() {
        super(Phrase.JDK_INFO);
    }

    public String getJvm() {
        return jvm;
    }

    public void setJvm(String jvm) {
        this.jvm = jvm;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    @Override
    public String toString() {
        return "JdkInfo{" +
            "jvm='" + jvm + '\'' +
            ", jvmVersion='" + jvmVersion + '\'' +
            ", jdkVersion='" + jdkVersion + '\'' +
            '}';
    }
}
