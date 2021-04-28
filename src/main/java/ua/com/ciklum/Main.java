package ua.com.ciklum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.core.util.AggregatorUtil;
import ua.com.ciklum.core.util.FileUtil;
import ua.com.ciklum.core.util.MatcherUtil;
import ua.com.ciklum.domain.TextOffset;

import java.util.List;
import java.util.Set;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalStateException("Please specify worlds to find and file to read");
        }
        LOGGER.debug("Start run application");
        final Set<String> wordsToFind = Set.of(args[0].split(","));
        final List<TextOffset> textOffsets = FileUtil.readBigTxtFile();
        AggregatorUtil.runAggregator(MatcherUtil.runMatchers(textOffsets, wordsToFind));
        LOGGER.debug("Stooped application");
    }
}
