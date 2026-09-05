package com.example.mnp.domain;

public enum PortingStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED;


    public static PortingStatus from(String status){
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Porting status decision cannot be null or empty.");
        }
        try {
            return PortingStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid porting status: " + status);
        }
    }
    public static boolean isValidDonorDecision(PortingStatus portingStatus){
        if(!portingStatus.equals(ACCEPTED)&&!portingStatus.equals(REJECTED)){
            return false;
        }
        return true;
    }
}
