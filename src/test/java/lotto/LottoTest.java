package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호가 1~45 범위를 벗어나면 예외가 발생한다. (45 초과)")
    @Test
    void 로또_번호가_45보다_크면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호는 1에서 45 사이여야 합니다");
    }

    @DisplayName("로또 번호가 1~45 범위를 벗어나면 예외가 발생한다. (1 미만)")
    @Test
    void 로또_번호가_1보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호는 1에서 45 사이여야 합니다");
    }


    @DisplayName("로또 번호에 특정 숫자가 포함되어 있는지 확인한다.")
    @Test
    void contains_메서드_테스트() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        assertThat(lotto.contains(3)).isTrue();
        assertThat(lotto.contains(7)).isFalse();
    }

    @DisplayName("두 로또 번호 간에 일치하는 번호의 개수를 반환한다.")
    @Test
    void countMatchingNumbers_메서드_테스트() {
        Lotto myLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(1, 3, 5, 7, 9, 11));

        assertThat(myLotto.countMatchingNumbers(winningLotto)).isEqualTo(3);
    }

    @DisplayName("일치하는 번호가 하나도 없는 경우 0을 반환한다.")
    @Test
    void countMatchingNumbers_메서드_0개_일치_테스트() {
        Lotto myLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(11, 12, 13, 14, 15, 16));

        assertThat(myLotto.countMatchingNumbers(winningLotto)).isEqualTo(0);
    }
}
