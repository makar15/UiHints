package codes.evolution.uihintslib;

import java.util.HashMap;
import java.util.Map;

public class Flow {

    private final Map<String, Hint> mHints = new HashMap<>();

    public Flow(Map<String, Hint> hints) {
        mHints.putAll(hints);
    }

    public Hint getHint(String name) {
        return mHints.get(name);
    }

    public Map<String, Hint> getHints() {
        return mHints;
    }

    boolean contains(String name) {
        return mHints.containsKey(name);
    }

    void remove(Hint hint) {
        mHints.remove(hint.getName());
    }

    void clear() {
        mHints.clear();
    }

    int size() {
        return mHints.size();
    }
}
