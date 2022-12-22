package brocoeur.example.analytics.service;

public enum ServiceRequestStatusStatus {

    IN_PROGRESS("IN_PROGRESS"),
    TODO("TODO"),
    DONE_WIN("DONE_WIN"),
    DONE_LOSS("DONE_LOSS"),
    REJECTED("REJECTED");

    public final String label;

    ServiceRequestStatusStatus(String label) {
        this.label = label;
    }
}
