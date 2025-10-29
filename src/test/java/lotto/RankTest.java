package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RankTest {

    @DisplayName("일치 개수와 보너스 여부에 따라 정확한 등수를 반환한다.")
    @ParameterizedTest
    @CsvSource({
            "6, false, FIRST",
            "5, true, SECOND",
            "5, false, THIRD",
            "4, false, FOURTH",
            "3, false, FIFTH",
            "2, true, NONE",
            "2, false, NONE",
            "0, false, NONE"
    })
    void determineRank_등수_결정_테스트(int matchCount, boolean bonusMatch, Rank expectedRank) {
        Rank rank = Rank.determineRank(matchCount, bonusMatch);
        assertThat(rank).isEqualTo(expectedRank);
    }

    @DisplayName("각 등수에 맞는 상금을 반환한다.")
    @Test
    void getPrize_상금_반환_테스트() {
        assertThat(Rank.FIRST.getPrize()).isEqualTo(2_000_000_000L);
        assertThat(Rank.SECOND.getPrize()).isEqualTo(30_000_000L);
        assertThat(Rank.THIRD.getPrize()).isEqualTo(1_500_000L);
        assertThat(Rank.FOURTH.getPrize()).isEqualTo(50_000L);
        assertThat(Rank.FIFTH.getPrize()).isEqualTo(5_000L);
        assertThat(Rank.NONE.getPrize()).isEqualTo(0L);
    }

    @DisplayName("각 등수에 맞는 출력 메시지를 반환한다.")
    @Test
    void getMessage_메시지_포맷_테스트() {
        assertThat(Rank.FIRST.getMessage()).isEqualTo("6개 일치 (2,000,000,000원)");
        assertThat(Rank.SECOND.getMessage()).isEqualTo("5개 일치, 보너스 볼 일치 (30,000,000원)");
        assertThat(Rank.THIRD.getMessage()).isEqualTo("5개 일치 (1,500,000원)");
        assertThat(Rank.FIFTH.getMessage()).isEqualTo("3개 일치 (5,000원)");
        assertThat(Rank.NONE.getMessage()).isEqualTo(""); // 꽝은 메시지 없음
    }
}