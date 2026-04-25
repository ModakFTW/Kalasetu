package com.kalasetu.model;

import jakarta.persistence.*;

@Entity
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterName;
    private String requesterEmail;
    private String requesterContact;

    @Column(length = 1000)
    private String message;

    private Double offeredBudget;

    @Enumerated(EnumType.STRING)
    private CommissionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public Commission() {
    }

    public Commission(String requesterName, String requesterEmail, String requesterContact, String message, Double offeredBudget,
            Artist artist) {
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.requesterContact = requesterContact;
        this.message = message;
        this.offeredBudget = offeredBudget;
        this.artist = artist;
        this.status = CommissionStatus.REQUESTED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterContact() {
        return requesterContact;
    }

    public void setRequesterContact(String requesterContact) {
        this.requesterContact = requesterContact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getOfferedBudget() {
        return offeredBudget;
    }

    public void setOfferedBudget(Double offeredBudget) {
        this.offeredBudget = offeredBudget;
    }

    public CommissionStatus getStatus() {
        return status;
    }

    public void setStatus(CommissionStatus status) {
        this.status = status;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
