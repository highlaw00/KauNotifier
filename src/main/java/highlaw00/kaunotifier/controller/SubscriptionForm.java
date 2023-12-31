package highlaw00.kaunotifier.controller;

import java.util.List;

public class SubscriptionForm {
    private List<Long> sourceList;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<Long> sourceList) {
        this.sourceList = sourceList;
    }
}
