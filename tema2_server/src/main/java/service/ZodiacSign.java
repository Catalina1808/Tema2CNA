package service;

public class ZodiacSign {
    private String sign;
    private String startDate;
    private String endDate;

    public ZodiacSign(String sign, String startDate, String endDate) {
        this.sign = sign;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ZodiacSign() {

    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getSign() {
        return sign;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}

