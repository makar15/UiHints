package codes.evolution.uihintslib;

import android.support.annotation.Nullable;

public class Hint {

    private final @Hint.HintName String mName;
    private final @Nullable @Hint.HintName String mNextHintName;
    private final @Nullable @Hint.HintName String mPrevHintName;

    public Hint(String name,
                @Nullable String prevHintName,
                @Nullable String nextHintName) {
        mName = name;
        mNextHintName = nextHintName;
        mPrevHintName = prevHintName;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public String getPrevHintName() {
        return mPrevHintName;
    }

    @Nullable
    public String getNextHintName() {
        return mNextHintName;
    }

    public boolean equals(Hint hint) {
        return mName.equals(hint.getName());
    }

    public @interface HintName {}
}
