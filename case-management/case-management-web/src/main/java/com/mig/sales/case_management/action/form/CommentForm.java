package com.mig.sales.case_management.action.form;

import org.apache.struts.action.ActionForm;

public class CommentForm extends ActionForm {

    private String commentText;
    private Long leadId;

    // Getters and Setters

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }
}