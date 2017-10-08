package codes.evolution.uihintslib;

import android.support.annotation.Nullable;

import codes.evolution.uihintslib.ui.HintParams;

public interface HintsStorage<ParamsType extends HintParams> {

    enum StorageEvent {
        ADDED, REMOVED, REMOVED_ALL
    }

    interface HintsStorageListener {
        void onStorageEvent(StorageEvent event, @Nullable @Hint.Name String hintName);
    }

    void addHint(@Hint.Name String hint, ParamsType params);

    void removeHint(@Hint.Name String hint);

    void removeAllHints();

    boolean hasHint(@Hint.Name String hint);

    boolean isEmpty();

    @Nullable
    ParamsType getHintParams(@Hint.Name String hint);

    void addListener(HintsStorageListener listener);

    void removeListener(HintsStorageListener listener);
}
