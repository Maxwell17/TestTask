package ua.com.ciklum.core.util;

import lombok.experimental.UtilityClass;
import ua.com.ciklum.core.AggregatorThread;
import ua.com.ciklum.domain.FoundText;

import java.util.SortedSet;

@UtilityClass
public class AggregatorUtil {

    public void runAggregator(final String toFind, final SortedSet<FoundText> foundTexts) {
        final Thread aggregatorThread = new AggregatorThread(toFind, foundTexts);
        aggregatorThread.start();
    }

}
