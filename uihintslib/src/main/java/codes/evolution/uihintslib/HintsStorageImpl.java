package codes.evolution.uihintslib;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codes.evolution.uihintslib.ui.HintParams;

public class HintsStorageImpl<ParamsType extends HintParams> implements HintsStorage<ParamsType>  {

    private final Map<String, ParamsType> mHints = new HashMap<>();
    private final List<HintsStorageListener> mListeners = new ArrayList<>();

    @Override
    public void addHint(@Hint.Name String hint, ParamsType params) {
        mHints.put(hint, params);
        notifyListeners(StorageEvent.ADDED, hint);
    }

    @Override
    public void removeHint(@Hint.Name String hint) {
        mHints.remove(hint);
        notifyListeners(StorageEvent.REMOVED, hint);
    }

    @Override
    public void removeAllHints() {
        mHints.clear();
        notifyListeners(StorageEvent.REMOVED_ALL, null);
    }

    @Override
    public boolean hasHint(@Hint.Name String hint) {
        return mHints.containsKey(hint);
    }

    @Override
    public boolean isEmpty() {
        return mHints.isEmpty();
    }

    @Override
    @Nullable
    public ParamsType getHintParams(@Hint.Name String hint) {
        return mHints.get(hint);
    }

    @Override
    public void addListener(HintsStorageListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(HintsStorageListener listener) {
        mListeners.remove(listener);
    }

    private void notifyListeners(StorageEvent event, @Hint.Name String hint) {
        for (HintsStorageListener listener : mListeners) {
            listener.onStorageEvent(event, hint);
        }
    }
}
