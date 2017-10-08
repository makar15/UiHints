package codes.evolution.uihints.wizard;

import java.util.HashMap;
import java.util.Map;

import codes.evolution.uihintslib.Hint;

public class Hints {

    public static final @Hint.Name String START_FLOW = "start_flow";
    public static final @Hint.Name String START_SECOND_WINDOW = "start_second_window";
    public static final @Hint.Name String FIRST_HINT = "first_hint";
    public static final @Hint.Name String SHOW_ADDITIONAL = "additional";
    public static final @Hint.Name String TEST_TEXT = "test_text";
    public static final @Hint.Name String FINISH_FLOW = "finish_flow";

    private static final Map<String, Hint> sHints = new HashMap<String, Hint>() {
        {
            put(START_FLOW, new Hint(START_FLOW, null, START_SECOND_WINDOW));
            put(START_SECOND_WINDOW, new Hint(START_SECOND_WINDOW, START_FLOW, null));
            put(FIRST_HINT, new Hint(FIRST_HINT, START_FLOW, SHOW_ADDITIONAL));
            put(SHOW_ADDITIONAL, new Hint(SHOW_ADDITIONAL, FIRST_HINT, null));
            put(TEST_TEXT, new Hint(TEST_TEXT, SHOW_ADDITIONAL, FINISH_FLOW));
            put(FINISH_FLOW, new Hint(FINISH_FLOW, null, null));
        }
    };

    public static Map<String, Hint> getAllHints() {
        return sHints;
    }
}
