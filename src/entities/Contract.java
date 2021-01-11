package entities;

/**
 * Retine informatiile unui contract
 */
public final class Contract {
    private final long consumerId;
    private final long price;
    private long remainedContractMonths;

    public Contract(final long consumerId, final long price, final long remainedContractMonths) {
        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    public void setRemainedContractMonths(long remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }

    public long getConsumerId() {
        return consumerId;
    }

    public long getPrice() {
        return price;
    }

    public long getRemainedContractMonths() {
        return remainedContractMonths;
    }
}
