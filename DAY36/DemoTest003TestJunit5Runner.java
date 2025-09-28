package DAY36;/*

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class DemoTest003TestJunit5Runner {
    public static void main(String[] args) {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(DemoTest003TestCases.class))
                .build());
        listener.getSummary().printTo(System.out);
    }
}*/
