package ua.com.ciklum.core;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.ciklum.domain.FoundText;

import java.util.SortedSet;

@AllArgsConstructor
public class AggregatorThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String toFind;

    private final SortedSet<FoundText> foundTexts;

    @Override
    public void run() {
        StringBuilder print = new StringBuilder(toFind + "-->[");
        for (final FoundText foundText : this.foundTexts) {
            print
                    .append("[lineOffset=")
                    .append(foundText.getLineOffset())
                    .append(",charOffset=")
                    .append(foundText.getCharOffset())
                    .append("]");
        }
        print.append("]");
        LOGGER.info(print);
    }

}
