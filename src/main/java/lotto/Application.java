package lotto;

import static camp.nextstep.edu.missionutils.Console.readLine;
import static camp.nextstep.edu.missionutils.Randoms.pickUniqueNumbersInRange;

import java.util.*;

public class Application {
    public static void main(String[] args) {

        //구입액수 입력
        int money = inputMoney();
        System.out.println();

        //구매로또번호 확인
        System.out.println(money / 1000 + "개를 구매했습니다.");
        List<Lotto> lotto = generateLotto(money / 1000);
        printLotto(lotto);

        //당첨번호 입력
        Lotto winningLotto = inputWinningNumbers();
        System.out.println();
        int bonus = inputBonus(winningLotto);
        System.out.println();

        //결과
        System.out.println("당첨 통계\n---");
        printResult(lotto, winningLotto, bonus, money);

    }

    //---------------------------------------------------
    public static int inputMoney() {
        while (true) {
            System.out.println("구입금액을 입력해 주세요.");
            try {
                return validateAndParseMoney(readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int validateAndParseMoney(String input) {
        int money;

        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있습니다.");
        }

        try {
            money = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 입력값이 숫자가 아닙니다. (입력: " + input + ")");
        }

        if (money <= 0) {
            throw new IllegalArgumentException("[ERROR] 입력값이 0 이거나 음수입니다.");
        }
        if (money % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 1000원으로 나누어 떨어지지 않습니다.");
        }

        return money;
    }
    //---------------------------------------------------

    public static List<Lotto> generateLotto(int n) {
        List<Lotto> allLotto = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> numbers = pickUniqueNumbersInRange(1, 45, 6);
            allLotto.add(new Lotto(numbers));
        }
        return allLotto;
    }

    public static void printLotto(List<Lotto> lotto) {
        for (Lotto oneLotto : lotto) {
            System.out.println(oneLotto.toString());
        }
        System.out.println();
    }

    //---------------------------------------------------
    public static Lotto inputWinningNumbers() {
        System.out.println("당첨 번호를 입력해 주세요.");
        while (true) {
            try {
                List<Integer> numbers = parseWinningNumbers(readLine());
                return new Lotto(numbers);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static List<Integer> parseWinningNumbers(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있습니다.");
        }

        String[] numberStrings = input.split(",");
        List<Integer> numbers = new ArrayList<>();

        for (String numStr : numberStrings) {
            numbers.add(parseSingleNumber(numStr.trim()));
        }

        return numbers;
    }

    private static int parseSingleNumber(String numStr) {
        try {
            return Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 숫자가 아닌 값이 포함되어 있습니다: " + numStr);
        }

    }
    //---------------------------------------------------

    //---------------------------------------------------
    public static int inputBonus(Lotto winningLotto) {
        System.out.println("보너스 번호를 입력해 주세요.");
        while (true) {
            try {
                return validateBonus(readLine(), winningLotto);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int validateBonus(String input, Lotto winningLotto) {
        int bonusNumber;
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있습니다.");
        }

        try {
            bonusNumber = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 입력값이 숫자가 아닙니다. (입력: " + input + ")");
        }

        if (bonusNumber < 1 || bonusNumber > 45) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1에서 45 사이여야 합니다.");
        }
        if (winningLotto.contains(bonusNumber)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호가 당첨 번호와 중복됩니다.");
        }

        return bonusNumber;

    }
    //---------------------------------------------------

    //---------------------------------------------------
    public static void printResult(
            List<Lotto> purchasedLotto, Lotto winningLotto, int bonusNumber, int money) {

        Map<Rank, Integer> stats = new EnumMap<>(Rank.class);
        
        //초기화
        for (Rank rank : Rank.getWinningRanks()) {
            stats.put(rank, 0);
        }

        for (Lotto lotto : purchasedLotto) {
            int matchCount = lotto.countMatchingNumbers(winningLotto);
            boolean bonusMatch = lotto.contains(bonusNumber);

            Rank rank = Rank.determineRank(matchCount, bonusMatch);

            if (rank != Rank.NONE) {
                stats.put(rank, stats.get(rank) + 1);
            }
        }

        List<Rank> displayOrder = Arrays.stream(Rank.getWinningRanks())
                .sorted(Comparator.comparingLong(Rank::getPrize))
                .toList();

        for (Rank rank : displayOrder) {
            System.out.println(rank.getMessage() + " - " + stats.get(rank) + "개");
        }

        long totalWinnings = calculateTotalWinnings(stats);
        double returnRate = calculateReturnRate(totalWinnings, money);
        System.out.printf("총 수익률은 %.1f%%입니다.%n", returnRate);
    }

    private static long calculateTotalWinnings(Map<Rank, Integer> stats) {
        return stats.entrySet().stream()
                .mapToLong(entry -> entry.getKey().getPrize() * entry.getValue())
                .sum();
    }

    private static double calculateReturnRate(long totalWinnings, int money) {
        return ((double) totalWinnings / (double) money) * 100.0;
    }
    //---------------------------------------------------
}
