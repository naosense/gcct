package io.github.pingao777.common.report;

import java.util.List;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/14.
 */
public interface Report {
    void report(List<? extends ResultData> result);
}
