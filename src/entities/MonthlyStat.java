package entities;

import java.util.ArrayList;

/**
 * Retine statisticile dintr-o luna
 */
public final class MonthlyStat {
    private final long month;
    private final ArrayList<Long> distributorsIds;

    public MonthlyStat(final long month, ArrayList<Long> distributors) {
        this.month = month;
        this.distributorsIds = distributors;
    }

    public long getMonth() {
        return month;
    }

    public ArrayList<Long> getDistributorsIds() {
        return distributorsIds;
    }
}
