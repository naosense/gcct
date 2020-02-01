package io.github.pingao777.common.parser;

import java.util.ArrayList;
import java.util.List;
import io.github.pingao777.common.enums.TokenType;
import io.github.pingao777.common.model.CommandLineFlags;
import io.github.pingao777.common.model.JdkInfo;
import io.github.pingao777.common.model.MemoryInfo;
import io.github.pingao777.common.model.ResultData;

import static io.github.pingao777.common.util.ParseUtils.parseByte;


/**
 * Created by pingao on 2020/2/1.
 */
public abstract class AbstractGCParser extends Parser {
    protected boolean isDateEnable;

    public AbstractGCParser(Lexer input, int k) {
        super(input, k);
    }

    @Override
    public List<ResultData> parse(ParseContext context) {
        List<ResultData> result = new ArrayList<>();
        result.addAll(parseHead(context));
        result.addAll(parseContent(context));
        result.addAll(parseFoot(context));
        return result;
    }

    protected void parseJdkInfo(List<ResultData> result) {
        JdkInfo info = new JdkInfo();
        if ("Java".equals(LV(1))) {
            info.setJvm("HotSpot");
            after(TokenType.LPAREN);
        } else if ("OpenJDK".equals(LV(1))) {
            info.setJvm("OpenJDK");
        }
        after(TokenType.LPAREN);
        info.setJvmVersion(LV(1) + LV(2) + LV(3) + LV(4));
        after(TokenType.LPAREN);
        info.setJdkVersion(LV(1) + LV(2) + LV(3));
        result.add(info);
    }

    protected void parseMemoryInfo(List<ResultData> result) {
        MemoryInfo info = new MemoryInfo();
        after(TokenType.COLON);
        info.setPage(parseByte(LV(1), LV(2)));
        after(TokenType.COMMA);
        info.setPhysicalTotal(parseByte(LV(2), LV(3)));
        info.setPhysicalFree(parseByte(LV(5), LV(6)));
        after(TokenType.COMMA);
        info.setSwapTotal(parseByte(LV(2), LV(3)));
        info.setSwapFree(parseByte(LV(5), LV(6)));
        result.add(info);
    }

    protected void parseFlags(List<ResultData> result) {
        CommandLineFlags flags = new CommandLineFlags();
        after(TokenType.COLON);
        flags.setFlags(getLine().substring(getCol() - 1));
        result.add(flags);
    }

    protected abstract List<ResultData> parseHead(ParseContext context);

    protected abstract List<ResultData> parseContent(ParseContext context);

    protected abstract List<ResultData> parseFoot(ParseContext context);
}
