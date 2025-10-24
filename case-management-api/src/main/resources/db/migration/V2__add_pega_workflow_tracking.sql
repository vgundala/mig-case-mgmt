-- =====================================================================
-- Add Pega Workflow Tracking
-- =====================================================================
-- This migration adds columns to track Pega workflow integration

-- Add Pega workflow tracking to leads
ALTER TABLE LEADS ADD (
    PEGA_WORKFLOW_ID VARCHAR2(100),
    PEGA_CASE_ID VARCHAR2(100),
    PEGA_STATUS VARCHAR2(50),
    PEGA_LAST_SYNC_DATE TIMESTAMP
);

-- Add indexes for Pega workflow queries
CREATE INDEX IDX_LEADS_PEGA_WORKFLOW_ID ON LEADS(PEGA_WORKFLOW_ID);
CREATE INDEX IDX_LEADS_PEGA_CASE_ID ON LEADS(PEGA_CASE_ID);
CREATE INDEX IDX_LEADS_PEGA_STATUS ON LEADS(PEGA_STATUS);

-- Add comments
COMMENT ON COLUMN LEADS.PEGA_WORKFLOW_ID IS 'Pega workflow instance ID';
COMMENT ON COLUMN LEADS.PEGA_CASE_ID IS 'Pega case ID for the lead';
COMMENT ON COLUMN LEADS.PEGA_STATUS IS 'Status in Pega workflow';
COMMENT ON COLUMN LEADS.PEGA_LAST_SYNC_DATE IS 'Last synchronization with Pega';
