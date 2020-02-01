package io.github.pingao777;

import java.util.List;
import java.util.stream.DoubleStream;
import io.github.pingao777.common.enums.GCType;
import io.github.pingao777.common.parser.Lexer;
import io.github.pingao777.common.parser.ParseContext;
import io.github.pingao777.common.parser.Parser;
import io.github.pingao777.common.report.Report;
import io.github.pingao777.common.model.ResultData;
import io.github.pingao777.common.util.ParseUtils;
import io.github.pingao777.jdk8.cms.Jdk8CMCLexer;
import io.github.pingao777.jdk8.cms.Jdk8CMCParser;
import io.github.pingao777.jdk8.cms.Jdk8CMSReporter;
import io.github.pingao777.jdk8.g1.Jdk8G1Parser;
import io.github.pingao777.jdk8.g1.Jdk8G1Reporter;


public class App {
    public static void main(String[] args) {
        ParseContext context = new ParseContext();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-h":
                    System.out.println("-h help\n"
                        + "-p gc log path\n"
                        + "-s startup start\n"
                        + "-e startup end");
                    return;
                case "-p":
                    context.setPath(args[i + 1]);
                    i++;
                    break;
                case "-s":
                    context.setStart(Double.parseDouble(args[i + 1]));
                    i++;
                    break;
                case "-e":
                    context.setEnd(Double.parseDouble(args[i + 1]));
                    i++;
                    break;
            }
        }
        if (context.getPath() == null) {
            System.err.println("path is null");
            return;
        }
        GCType gc = ParseUtils.whichGC(context.getPath());
        switch (gc) {
            case CMS: {
                Lexer lexer = new Jdk8CMCLexer(context.getPath());
                Parser parser = new Jdk8CMCParser(lexer, 15);
                List<ResultData> result = parser.parse(context);
                Report report = new Jdk8CMSReporter();
                report.report(result);
            }
            break;
            case G1: {
                Jdk8CMCLexer lexer = new Jdk8CMCLexer(context.getPath());
                Jdk8G1Parser parser = new Jdk8G1Parser(lexer, 15);
                List<ResultData> result = parser.parse(context);
                Report report = new Jdk8G1Reporter();
                report.report(result);
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + gc);
        }
    }
}
