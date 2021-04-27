package ua.com.ciklum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.core.util.AggregatorUtil;
import ua.com.ciklum.core.util.FileUtil;
import ua.com.ciklum.core.util.MatcherUtil;
import ua.com.ciklum.domain.FoundText;
import ua.com.ciklum.domain.TextOffset;

import java.util.List;
import java.util.SortedSet;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalStateException("Please specify world to find and file to read");
        }
        final String toFind = args[0];
        final List<TextOffset> textOffsets = FileUtil.readFile(args[1]);
        LOGGER.debug("File was successfully read, word to find {}", toFind);
        final SortedSet<FoundText> foundTexts = MatcherUtil.runMatchers(textOffsets, toFind);
        AggregatorUtil.runAggregator(toFind, foundTexts);
    }

}
