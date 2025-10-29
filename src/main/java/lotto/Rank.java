package lotto;

import java.text.NumberFormat;
import java.util.Arrays;

public enum Rank {

    FIRST(6, 2_000_000_000L, false),
    SECOND(5, 30_000_000L, true),
    THIRD(5, 1_500_000L, false),
    FOURTH(4, 50_000L, false),
    FIFTH(3, 5_000L, false),
    NONE(0, 0L, false);

    private final int matchCount;
    private final long prize;
    private final boolean bonusMatchRequired;

    Rank(int matchCount, long prize, boolean bonusMatchRequired) {
        this.matchCount = matchCount;
        this.prize = prize;
        this.bonusMatchRequired = bonusMatchRequired;
    }

    // Getter
    public long getPrize() {
        return prize;
    }

    public static Rank determineRank(int matchCount, boolean bonusMatch) {
        if (matchCount == FIRST.matchCount) {
            return FIRST;
        }
        if (matchCount == SECOND.matchCount && bonusMatch == SECOND.bonusMatchRequired) {
            return SECOND;
        }
        if (matchCount == THIRD.matchCount && !THIRD.bonusMatchRequired) {
            return THIRD;
        }
        if (matchCount == FOURTH.matchCount) {
            return FOURTH;
        }
        if (matchCount == FIFTH.matchCount) {
            return FIFTH;
        }
        return NONE;
    }

    public String getMessage() {
        String formattedPrize = NumberFormat.getNumberInstance().format(prize);

        if (this == SECOND) {
            return String.format("%d개 일치, 보너스 볼 일치 (%s원)", matchCount, formattedPrize);
        }
        if (this == FIRST || this == THIRD || this == FOURTH || this == FIFTH) {
            return String.format("%d개 일치 (%s원)", matchCount, formattedPrize);
        }
        return "";
    }

    public static Rank[] getWinningRanks() {
        return Arrays.stream(values())
                .filter(rank -> rank != NONE)
                .toArray(Rank[]::new);
    }
}

