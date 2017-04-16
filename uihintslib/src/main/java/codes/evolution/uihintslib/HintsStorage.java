package codes.evolution.uihintslib;

import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codes.evolution.uihintslib.ui.HintParams;
import codes.evolution.uihintslib.ui.ShadowViewType;

public class HintsStorage {

    public enum StorageEvent {
        ADDED, REMOVED, REMOVED_ALL
    }

    public interface HintsStorageListener {
        void onStorageEvent(StorageEvent event, @Nullable @Hint.HintName String hintName);
    }

    private final Map<String, HintParams> mHints = new HashMap<>();
    private final List<HintsStorageListener> mListeners = new ArrayList<>();

    public void addHint(@Hint.HintName String hint, View target) {
        addHint(hint, new HintParams(target));
    }

    public void addHint(@Hint.HintName String hint, View target, @HintViewType int viewType) {
        addHint(hint, new HintParams(target, viewType));
    }

    public void addHint(@Hint.HintName String hint, View target, ShadowViewType type) {
        addHint(hint, new HintParams(target, type));
    }

    public void addHint(@Hint.HintName String hint, View target, ShadowViewType shadowType,
                        @HintViewType int viewType) {
        addHint(hint, new HintParams(target, shadowType, viewType));
    }

    public void addHint(@Hint.HintName String hint, HintParams params) {
        mHints.put(hint, params);
        notifyListeners(StorageEvent.ADDED, hint);
    }

    public void removeHint(@Hint.HintName String hint) {
        mHints.remove(hint);
        notifyListeners(StorageEvent.REMOVED, hint);
    }

    public void removeAllHints() {
        mHints.clear();
        notifyListeners(StorageEvent.REMOVED_ALL, null);
    }

    public boolean hasHint(@Hint.HintName String hint) {
        return mHints.containsKey(hint);
    }

    public boolean isEmpty() {
        return mHints.isEmpty();
    }

    @Nullable
    public HintParams getHintParams(@Hint.HintName String hint) {
        if (hasHint(hint)) {
            return mHints.get(hint);
        }
        return null;
    }

    public void addListener(HintsStorageListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(HintsStorageListener listener) {
        mListeners.remove(listener);
    }

    private void notifyListeners(StorageEvent event, @Hint.HintName String hint) {
        for (HintsStorageListener listener : mListeners) {
            listener.onStorageEvent(event, hint);
        }
    }
}
