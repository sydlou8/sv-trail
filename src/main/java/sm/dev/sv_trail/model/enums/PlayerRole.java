package sm.dev.sv_trail.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerRole {
    BEGINNER_DEV(1000, 80, 10, 0, 30, 100),
    INTERMEDIATE_DEV(3000, 50, 20, 0, 20, 150),
    ADVANCED_DEV(5000, 30, 30, 0, 10, 200);

    private final int startingCash;
    private final int startingMorale;
    private final int startingHype;
    private final int startingBugCount;
    private final int startingCoffee;
    private final int freelancePay;
}
