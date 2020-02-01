package io.github.pingao777.jdk8.g1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.CommandLineFlags;
import io.github.pingao777.common.model.CommonTime;
import io.github.pingao777.common.model.JdkInfo;
import io.github.pingao777.common.model.MemoryInfo;
import io.github.pingao777.common.model.Token;
import io.github.pingao777.common.parser.AbstractGCParser;
import io.github.pingao777.common.parser.Lexer;
import io.github.pingao777.common.parser.ParseContext;
import io.github.pingao777.common.parser.Parser;
import io.github.pingao777.common.model.ResultData;
import io.github.pingao777.common.enums.TokenType;
import io.github.pingao777.jdk8.g1.model.G1Cleanup;
import io.github.pingao777.jdk8.g1.model.G1Concurrent;
import io.github.pingao777.jdk8.g1.model.G1FullGC;
import io.github.pingao777.jdk8.g1.model.G1Pause;
import io.github.pingao777.jdk8.g1.model.G1Remark;

import static io.github.pingao777.common.util.ParseUtils.parseByte;


/**
 * Created by pingao on 2020/1/16.
 */
public class Jdk8G1Parser extends AbstractGCParser {
    public Jdk8G1Parser(Lexer input, int k) {
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
                        switch (LV(5)) {
                            case "pause":
                                parseG1Pause(result);
                                break;
                            case "remark":
                                parseG1Remark(result);
                                break;
                            case "cleanup":
                                parseG1Cleanup(result);
                                break;
                            case "concurrent":
                                parseG1Concurrent(result);
                                break;
                        }
                        break;
                    case "Full":
                        parseG1FullGC(result);
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

    private void parseG1Pause(List<ResultData> result) {
        G1Pause item = new G1Pause();
        item.setStartup(Double.parseDouble(LV(1)));
        if ("Humongous".equals(LV(9))) {
            item.setPhrase(Phrase.G1_HUMONGOUS_PAUSE);
        }
        after(TokenType.RPAREN);
        after(TokenType.LPAREN);
        item.setName(LV(1));
        if ("(".equals(LV(3))) {
            item.setOption(between(TokenType.LPAREN, TokenType.RPAREN));
        }
        after(TokenType.COMMA);
        item.setDuration(Double.parseDouble(LV(1)));
        after(new Token("Eden", TokenType.NAME));
        after(TokenType.COLON);
        item.setEdenBefore(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setEdenTotalBefore(parseByte(LV(1), LV(2)));
        after(TokenType.GT);
        item.setEdenAfter(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setEdenTotalAfter(parseByte(LV(1), LV(2)));
        after(TokenType.COLON);
        item.setSurvivorsBefore(parseByte(LV(1), LV(2)));
        after(TokenType.GT);
        item.setSurvivorsAfter(parseByte(LV(1), LV(2)));
        after(TokenType.COLON);
        item.setHeapBefore(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setHeapTotalBefore(parseByte(LV(1), LV(2)));
        after(TokenType.GT);
        item.setHeapAfter(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setHeapTotalAfter(parseByte(LV(1), LV(2)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseG1Remark(List<ResultData> result) {
        G1Remark item = new G1Remark();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setFinalizeMark(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setRefProc(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setUploading(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setDuration(Double.parseDouble(LV(1)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseG1Cleanup(List<ResultData> result) {
        G1Cleanup item = new G1Cleanup();
        item.setStartup(Double.parseDouble(LV(1)));
        item.setHeapBefore(parseByte(LV(6), LV(7)));
        item.setHeapAfter(parseByte(LV(10), LV(11)));
        item.setHeapTotal(parseByte(LV(13), LV(14)));
        after(TokenType.COMMA);
        item.setDuration(Double.parseDouble(LV(1)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private void parseG1Concurrent(List<ResultData> result) {
        G1Concurrent item = new G1Concurrent();
        item.setStartup(Double.parseDouble(LV(1)));

        switch (LV(7)) {
            case "mark":
                switch (LV(9)) {
                    case "end":
                        after(TokenType.COMMA);
                        item.setDuration(Double.parseDouble(LV(1)));
                        result.add(item);
                        break;
                }
                break;
            case "cleanup":
                switch (LV(9)) {
                    case "end":
                        item.setPhrase(Phrase.G1_CONCURRENT_CLEANUP);
                        after(TokenType.COMMA);
                        item.setDuration(Double.parseDouble(LV(1)));
                        result.add(item);
                        break;
                }
                break;
            case "root":
                after(new Token("root", TokenType.NAME));
                if (LV(6).equals("end")) {
                    item.setPhrase(Phrase.G1_CONCURRENT_ROOT_REGION_SCAN);
                    after(TokenType.COMMA);
                    item.setDuration(Double.parseDouble(LV(1)));
                    result.add(item);
                }
                break;
        }
    }

    private void parseG1FullGC(List<ResultData> result) {
        G1FullGC item = new G1FullGC();
        item.setStartup(Double.parseDouble(LV(1)));
        after(TokenType.COMMA);
        item.setDuration(Double.parseDouble(LV(1)));
        after(new Token("Eden", TokenType.NAME));
        after(TokenType.COLON);
        item.setEdenBefore(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setEdenTotalBefore(parseByte(LV(1), LV(2)));
        after(TokenType.GT);
        item.setEdenAfter(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setEdenTotalAfter(parseByte(LV(1), LV(2)));
        after(TokenType.COLON);
        item.setSurvivorsBefore(parseByte(LV(1), LV(2)));
        after(TokenType.GT);
        item.setSurvivorsAfter(parseByte(LV(1), LV(2)));
        after(TokenType.COLON);
        item.setHeapBefore(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setHeapTotalBefore(parseByte(LV(1), LV(2)));
        after(TokenType.GT);
        item.setHeapAfter(parseByte(LV(1), LV(2)));
        after(TokenType.LPAREN);
        item.setHeapTotalAfter(parseByte(LV(1), LV(2)));
        CommonTime time = parseCommonTime();
        item.setUser(time.getUser());
        item.setSys(time.getSys());
        item.setReal(time.getReal());
        result.add(item);
    }

    private CommonTime parseCommonTime() {
        CommonTime time = new CommonTime();
        after(TokenType.EQUALS);
        time.setUser(Double.parseDouble(LV(1)));
        time.setSys(Double.parseDouble(LV(4)));
        time.setUser(Double.parseDouble(LV(8)));
        return time;
    }
}
