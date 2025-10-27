package dev.app.rentingCar_boot.utils;

public class PopulateStatus {

    private boolean status;
    private String message;
    private int qty;

    public PopulateStatus(boolean status, String message, int qty) {
        this.status = status;
        this.message = message;
        this.qty = qty;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "PopulateStatus [status=" + status + ", message=" + message + ", qty=" + qty + "]";
    }
}
