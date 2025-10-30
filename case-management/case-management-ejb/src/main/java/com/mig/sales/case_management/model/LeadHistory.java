package com.mig.sales.case_management.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LEAD_HISTORY")
public class LeadHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lead_history_seq")
    @SequenceGenerator(name = "lead_history_seq", sequenceName = "LEAD_HISTORY_SEQ", allocationSize = 1)
    @Column(name = "HISTORY_ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "LEAD_ID")
    private Lead lead;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @Column(name = "ACTION")
    private String action;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}