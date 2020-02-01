package io.github.pingao777.jdk8.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.github.pingao777.common.model.CommandLineFlags;
import io.github.pingao777.common.model.CommonTime;
import io.github.pingao777.common.model.JdkInfo;
import io.github.pingao777.common.model.MemoryInfo;
import io.github.pingao777.common.parser.AbstractGCParser;
import io.github.pingao777.common.parser.Lexer;
import io.github.pingao777.common.parser.ParseContext;
import io.github.pingao777.common.parser.Parser;
import io.github.pingao777.common.model.ResultData;
import io.github.pingao777.common.enums.TokenType;
import io.github.pingao777.common.util.ParseUtils;
import io.github.pingao777.jdk8.cms.model.AllocationFailure;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentAbortable;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentMark;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentPreclean;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentReset;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentSweep;
import io.github.pingao777.jdk8.cms.model.CMSFinalRemark;
import io.github.pingao777.jdk8.cms.model.CMSInitialMark;
import io.github.pingao777.jdk8.cms.model.ConcurrentModeFailure;
import io.github.pingao777.jdk8.cms.model.FullGC;
import io.github.pingao777.jdk8.cms.model.GCLockerInitiatedGC;

import static io.github.pingao777.common.util.ParseUtils.parseByte;


/**
 * Created by pingao on 2019/10/28.
 */
public class Jdk8CMCParser extends AbstractGCParser {

    public Jdk8CMCParser(Lexer input, int k) {
        super(input, k);
    }

    @Override
    protected List<ResultData> parseHead(ParseContext context) {
        List<ResultData> result = new ArrayList<>();
        while (LA(1) != TokenType.EOF) {
            if (LA(1) == TokenType.NAME) {
                switch (LV(1)) {
                    case "OpenJDK":
                    case "Java":
                        parseJdkInfo(result);
                        break;
                    case "Memory":
                        parseMemoryInfo(result);
                        break;
                    case "CommandLine":
                        if (getLine().contains("PrintGCDateStamps")) {
                            isDateEnable = true;
                        }
                        parseFlags(result);
                        break;
                }
            } else {
                break;
            }
            after(TokenType.CR);
        }
        return result;
    }

    @Override
    protected List<ResultData> parseContent(ParseContext context) {
        List<ResultData> result = new ArrayList<>();
        while (LA(1) != TokenType.EOF) {
            if (isDateEnable) {
                after(TokenType.COLON);
                after(TokenType.COLON);
                after(TokenType.COLON);
            }
            if (LA(1) == TokenType.NUM) {
                double startup = Double.parseDouble(LV(1));
                if (startup < context.getStart()) {
                    after(TokenType.CR);
                    continue;
                }
                if (startup > context.getEnd()) {
                    break;
                }
                switch (LV(4)) {
                    case "GC":
                        switch (LV(6)) {
                            case "Allocation":
                                parseAllocationFailure(result);
                                break;
                            case "CMS":
                                switch (LV(7)) {
                                    case "Initial":
                                        parseInitialMark(result);
                                        break;
                                    case "Final":
                                        parseFinalRemark(result);
                                        break;
                                }
                                break;
                            case "GCLocker":
                                parseGCLocker(result);
                                break;
                        }
                        break;
                    case "CMS":
                        switch (LV(8)) {
                            case "abortable":
                                switch (LV(11)) {
                                    case "-":
                                        break;
                                    case ":":
                                        parseAbortable(result);
                                        break;
                                }
                                break;
                            case "mark":
                                switch (LV(9)) {
                                    case "-":
                                        break;
                                    case ":":
                                        parseConcurrentMark(result);
                                        break;
                                }
                                break;
                            case "preclean":
                                switch (LV(9)) {
                                    case "-":
                                        break;
                                    case ":":
                                        parsePreclean(result);
                                        break;
                                }

                                break;
                            case "reset":
                                switch (LV(9)) {
                                    case "-":
                                        break;
                                    case ":":
                                        parseReset(result);
                                        break;
                                }
                                break;
                            case "sweep":
                                switch (LV(9)) {
                                    case "-":
                                        break;
                                    case ":":
                                        parseSweep(result);
                                        break;
                                }
                        }
                        break;
                    case "Full":
                        parseFullGC(result);
                        break;
                }
            } else {
                if ("Heap".equals(LV(1))) {
                    break;
                }
            }
            after(TokenType.CR);
        }
        return result;
    }

    @Override
    protected List<ResultData> parseFoot(ParseContext context) {
        return Collections.emptyList();
    }

    private void parseAllocationFailure(List<ResultData> result) {
        AllocationFailure item = new AllocationFailure();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.LBRACK);
        after(TokenType.LBRACK);
        after(TokenType.COLON);
        item.setYoungBefore(parseByte(LV(1), LV(2)));
        item.setYoungAfter(parseByte(LV(5), LV(6)));
        item.setYoungTotal(parseByte(LV(8), LV(9)));
        after(TokenType.COMMA);
        item.setYoungDuration(Double.parseDouble(LV(1)));
        after(TokenType.RBRACK);
        if (LA(2) == TokenType.NAME) {
            item.setHeapBefore(parseByte(LV(1), LV(2)));
            item.setHeapAfter(parseByte(LV(5), LV(6)));
            item.setHeapTotal(parseByte(LV(8), LV(9)));
            after(TokenType.COMMA);
            item.setDuration(Double.parseDouble(LV(1)));
            CommonTime time = parseCommonTime();
            item.setUser(time.getUser());
            item.setSys(time.getSys());
            item.setReal(time.getReal());
            result.add(item);
        } else {
            after(TokenType.LBRACK);
            if (LA(2) == TokenType.COLON) {
                after(TokenType.RBRACK);
                item.setHeapBefore(parseByte(LV(1), LV(2)));
                item.setHeapAfter(parseByte(LV(5), LV(6)));
                item.setHeapTotal(parseByte(LV(8), LV(9)));
                result.add(item);
            }
            CommonTime time = parseCommonTime();
            item.setUser(time.getUser());
            item.setSys(time.getSys());
            item.setReal(time.getReal());
        }
        parseConcurrentModeFailure(result, item.getStartup() + item.getReal());
    }

    private CommonTime parseCommonTime() {
        CommonTime time = new CommonTime();
        after(TokenType.EQUALS);
        time.setUser(Double.parseDouble(LV(1)));
        time.setSys(Double.parseDouble(LV(4)));
        time.setUser(Double.parseDouble(LV(8)));
        return time;
    }

    private void parseConcurrentModeFailure(List<ResultData> result, double startup) {
        before(TokenType.CR);
        if (LA(2) == TokenType.LPAREN) {
            after(TokenType.CR);
            if (getLine().contains("concurrent mode failure")) {
                ConcurrentModeFailure item = new ConcurrentModeFailure();
                item.setStartup(startup);
                after(TokenType.COLON);
                item.setOldBefore(parseByte(LV(1), LV(2)));
                item.setOldAfter(parseByte(LV(5), LV(6)));
                item.setOldTotal(parseByte(LV(8), LV(9)));
                after(TokenType.COMMA);
                item.setOldDuration(Double.parseDouble(LV(1)));
                after(TokenType.RBRACK);
                item.setHeapBefore(parseByte(LV(1), LV(2)));
                item.setHeapAfter(parseByte(LV(5), LV(6)));
                item.setHeapTotal(parseByte(LV(8), LV(9)));
                after(TokenType.COLON);
                item.setMetaspaceBefore(parseByte(LV(1), LV(2)));
                item.setMetaspaceAfter(parseByte(LV(5), LV(6)));
                item.setMetaspaceTotal(parseByte(LV(8), LV(9)));
                after(TokenType.COMMA);
                item.setDuration(Double.parseDouble(LV(1)));
                CommonTime time = parseCommonTime();
                item.setUser(time.getUser());
                item.setSys(time.getSys());
                item.setReal(time.getReal());
                result.add(item);
            }
        }
    }

    private void parseInitialMark(List<ResultData> result) {
        CMSInitialMark item = new CMSInitialMark();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.COLON);
        after(TokenType.COLON);
        item.setOldOccupy(parseByte(LV(1), LV(2)));
        item.setOldAvailable(parseByte(LV(4), LV(5)));
        after(TokenType.RBRACK);
        item.setHeapOccupy(parseByte(LV(1), LV(2)));
        item.setHeapTotal(parseByte(LV(4), LV(5)));
        item.setDuration(Double.parseDouble(LV(8)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseFinalRemark(List<ResultData> result) {
        CMSFinalRemark item = new CMSFinalRemark();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.COLON);
        after(TokenType.COLON);
        item.setYoungOccupy(parseByte(LV(1), LV(2)));
        item.setYoungTotal(parseByte(LV(4), LV(5)));
        after(TokenType.COMMA);
        item.setRescanDuration(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setWeakRefProcessDuration(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setClassUploadDuration(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setScrubSymbolTableDuration(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setScrubStringTableDuration(Double.parseDouble(LV(1)));
        after(TokenType.COLON);
        item.setOldOccupy(parseByte(LV(1), LV(2)));
        item.setOldTotal(parseByte(LV(4), LV(5)));
        after(TokenType.RBRACK);
        item.setHeapOccupy(parseByte(LV(1), LV(2)));
        item.setHeapTotal(parseByte(LV(4), LV(5)));
        item.setDuration(Double.parseDouble(LV(8)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseGCLocker(List<ResultData> result) {
        GCLockerInitiatedGC item = new GCLockerInitiatedGC();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.COLON);
        after(TokenType.COLON);
        after(TokenType.COLON);
        item.setYoungBefore(parseByte(LV(1), LV(2)));
        item.setYoungAfter(parseByte(LV(5), LV(6)));
        item.setYoungTotal(parseByte(LV(8), LV(9)));
        after(TokenType.COMMA);
        item.setCleanUpDuration(Double.parseDouble(LV(1)));
        after(TokenType.RBRACK);
        item.setHeapBefore(parseByte(LV(1), LV(2)));
        item.setHeapAfter(parseByte(LV(5), LV(6)));
        item.setHeapTotal(parseByte(LV(8), LV(9)));
        after(TokenType.COMMA);
        item.setDuration(Double.parseDouble(LV(1)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseAbortable(List<ResultData> result) {
        CMSConcurrentAbortable item = new CMSConcurrentAbortable();
        item.setStartup(Double.parseDouble(LV(1)));
        item.setDuration(Double.parseDouble(LV(12)));
        item.setClock(Double.parseDouble(LV(14)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseConcurrentMark(List<ResultData> result) {
        CMSConcurrentMark item = new CMSConcurrentMark();
        item.setStartup(Double.parseDouble(LV(1)));
        item.setDuration(Double.parseDouble(LV(10)));
        item.setClock(Double.parseDouble(LV(12)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
        parseConcurrentModeFailure(result, item.getStartup() + item.getReal());
    }

    private void parsePreclean(List<ResultData> result) {
        CMSConcurrentPreclean item = new CMSConcurrentPreclean();
        item.setStartup(Double.parseDouble(LV(1)));
        item.setDuration(Double.parseDouble(LV(10)));
        item.setClock(Double.parseDouble(LV(12)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseReset(List<ResultData> result) {
        CMSConcurrentReset item = new CMSConcurrentReset();
        item.setStartup(Double.parseDouble(LV(1)));
        item.setDuration(Double.parseDouble(LV(10)));
        item.setClock(Double.parseDouble(LV(12)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseSweep(List<ResultData> result) {
        CMSConcurrentSweep item = new CMSConcurrentSweep();
        item.setStartup(Double.parseDouble(LV(1)));
        item.setDuration(Double.parseDouble(LV(10)));
        item.setClock(Double.parseDouble(LV(12)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseFullGC(List<ResultData> result) {
        FullGC item = new FullGC();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.LBRACK);
        after(TokenType.LBRACK);
        if (LA(2) == TokenType.COLON) {
            after(TokenType.COLON);
            item.setOldBefore(parseByte(LV(1), LV(2)));
            item.setOldAfter(parseByte(LV(5), LV(6)));
            item.setOldTotal(parseByte(LV(8), LV(9)));
            item.setOldDuration(Double.parseDouble(LV(12)));
            after(TokenType.RBRACK);
            item.setHeapBefore(parseByte(LV(1), LV(2)));
            item.setHeapAfter(parseByte(LV(5), LV(6)));
            item.setHeapTotal(parseByte(LV(8), LV(9)));
            after(TokenType.COLON);
            item.setMetaspaceBefore(parseByte(LV(1), LV(2)));
            item.setMetaspaceAfter(parseByte(LV(5), LV(6)));
            item.setMetaspaceTotal(parseByte(LV(8), LV(9)));
            item.setDuration(Double.parseDouble(LV(13)));
            CommonTime time = parseCommonTime();
            item.setUser(time.getUser());
            item.setSys(time.getSys());
            item.setReal(time.getReal());
            result.add(item);
        }
        parseConcurrentModeFailure(result, item.getStartup() + item.getReal());
    }


}
