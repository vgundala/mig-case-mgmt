# Synthetic Data Generation for Lead Management Service

## Overview
This document describes the comprehensive synthetic data generated for testing and development of the Lead Management Service. The data includes 100 leads, 15 users, and extensive lead history records covering various business scenarios.

## Files
- `synthetic-data.sql` - Complete SQL script to populate the database with synthetic data

## Data Statistics

### Users (15 total)
- **Sales Managers (3)**
  - Sarah Anderson
  - Michael Chen
  - David Rodriguez

- **Sales Persons (12)**
  - Emily Johnson, James Wilson, Olivia Brown, William Davis
  - Sophia Miller, Benjamin Martinez, Ava Garcia, Lucas Lopez (inactive)
  - Mia Lee, Henry Taylor, Isabella Thomas, Alexander Jackson

**Note:** One user (Lucas Lopez) is marked as inactive to test scenarios with inactive users.

### Leads (100 total)

#### By Status
- **NEW**: ~15 leads (unassigned, awaiting distribution)
- **ASSIGNED**: ~20 leads (assigned to sales persons)
- **IN_PROGRESS**: ~25 leads (active sales process)
- **PRE_CONVERSION**: ~15 leads (proposal stage, near conversion)
- **CONVERTED**: 10 leads (successfully converted clients)
- **REJECTED**: 5 leads (not suitable for our services)

#### By Value Category
- **High-Value (> $1M)**: 10 leads
  - Examples: GlobalTech Enterprises ($2.5M), Apex Manufacturing ($3.2M)
  - Typically in PRE_CONVERSION or IN_PROGRESS status
  
- **Medium-Value ($250K - $999K)**: 35 leads
  - Examples: Moore & Associates ($450K), Clark Construction ($680K)
  - Various industries: Legal, Construction, Marketing, Consulting, etc.
  
- **Low-Value (< $250K)**: 25 leads
  - Examples: Murphy Auto Repair ($85K), Gonzalez Cleaning ($45K)
  - Small businesses, some converted, some rejected
  
- **Converted Leads**: 10 leads (mix of value categories)
- **Rejected Leads**: 5 leads (mostly low-value, budget constraints)

#### By Lead Source
- **Partner Referral**: ~25 leads (typically higher value and scores)
- **Webinar**: ~25 leads (various industries and values)
- **Website Signup**: ~30 leads (mixed value and scores)
- **Cold Call**: ~20 leads (typically lower scores)

#### By Industry
- **Technology**: Multiple leads (startups to large enterprises)
- **Finance**: Wealth management, financial services
- **Manufacturing**: Small to large companies
- **Healthcare**: Medical practices, pharmaceutical companies
- **Real Estate**: Investment firms, REITs
- **Retail**: Chains, small businesses
- **Construction**: Contractors, construction companies
- **Legal**: Law firms, estate planning
- **Consulting**: Management consulting firms
- **Services**: Various service businesses
- **Food Services**: Restaurants, catering
- **Non-Profit**: Foundations, organizations
- **And more...**

#### By Company Size
- **Large**: High-value enterprise clients
- **Medium**: Mid-market businesses
- **Small**: Small businesses and startups

#### By Location
- Major US cities: New York, San Francisco, Chicago, Boston, Los Angeles, Houston, Dallas, Philadelphia, Atlanta, Seattle
- Geographic distribution for testing location-based scenarios

### Lead Scores
- **Range**: 0-100
- **High (>90)**: Typically high-value leads, partner referrals
- **Medium (50-89)**: Most leads fall in this range
- **Low (<50)**: Lower-value leads, some may be rejected

### Lead History
- **~150+ history records** tracking lead progression
- **Action Types**:
  - `SYSTEM`: Automated actions (creation, assignment)
  - `USER_ACTION`: Manual actions by sales persons (calls, meetings, proposals)
  - `WORKFLOW`: Workflow-triggered actions (status changes, escalations)
  
- **Common Actions**:
  - Created, Assigned, Qualification, Needs Assessment
  - Discovery Meeting, Proposal Submitted, Proposal Accepted
  - Converted, Onboarding, Rejected
  - Escalated, Follow-up calls, Check-ins

## Scenarios Covered

### 1. High-Value Lead Conversion
- Lead ID 1 (Robert Mitchell - $2.5M): Complete conversion path from NEW → ASSIGNED → IN_PROGRESS → PRE_CONVERSION
- Lead ID 7 (Mark Stevens - $1.2M): Successfully converted with full history

### 2. Rejected Leads
- Lead ID 41 (Linda Cox): Photography studio - budget too small
- Lead ID 48 (Steven James): Fitness center - budget constraints
- Lead ID 60 (Amanda Wood): Florist shop - minimal investment capacity

### 3. Escalation Scenarios
- Lead ID 12: Escalated to manager for complex needs review

### 4. Multiple Contact Attempts
- Lead ID 23: Multiple contact attempts before successful connection

### 5. Various Industries
- Technology startups, manufacturing, healthcare, finance, real estate, retail, etc.

### 6. Different Lead Sources
- Partner referrals (high quality), webinars, website signups, cold calls

### 7. Status Transitions
- Complete workflows showing leads moving through all status stages
- Some leads stuck at certain stages (realistic scenario)

### 8. Time-Based Scenarios
- Leads created over the past 90 days with various timestamps
- Recent leads vs older leads for testing recency-based features

### 9. Assigned vs Unassigned
- Mix of assigned leads (to various sales persons) and unassigned (NEW) leads
- Some leads assigned to inactive user (Lucas Lopez) for edge case testing

### 10. High-Value Leads (>$1M)
- 10 leads specifically categorized as high-value for testing special handling
- These should appear in `v_high_value_leads` view

## Usage

### Loading the Data

1. **Ensure database schema is created**
   ```bash
   # Run the DDL script first
   psql -U lead_mgmt_user -d lead_management -f ../documentation/pega_postgresql_ddl.sql
   ```

2. **Load the synthetic data**
   ```bash
   psql -U lead_mgmt_user -d lead_management -f synthetic-data.sql
   ```

### Verifying Data

```sql
-- Check user count
SELECT COUNT(*) FROM app_users;
-- Expected: 15

-- Check lead count
SELECT COUNT(*) FROM leads;
-- Expected: 100

-- Check status distribution
SELECT status, COUNT(*) FROM leads GROUP BY status;

-- Check high-value leads
SELECT COUNT(*) FROM leads WHERE potential_value >= 1000000;
-- Expected: 10

-- Check lead history count
SELECT COUNT(*) FROM lead_history;
-- Expected: ~150+

-- Check converted leads
SELECT COUNT(*) FROM leads WHERE status = 'CONVERTED';
-- Expected: 10

-- Check rejected leads
SELECT COUNT(*) FROM leads WHERE status = 'REJECTED';
-- Expected: 5
```

## Test Scenarios

### 1. Lead Distribution
- Test distributing NEW leads to sales persons
- Check workload balancing (some users have more leads than others)

### 2. Lead Scoring
- Verify leads have appropriate scores for their value and source
- Test filtering/sorting by lead score

### 3. Status Management
- Test status transitions and validation
- Test workflow rules for different statuses

### 4. High-Value Lead Handling
- Test special handling for leads > $1M
- Verify they appear in high-value leads view

### 5. User Assignment
- Test assigning leads to active vs inactive users
- Test reassignment scenarios

### 6. History Tracking
- Verify all lead actions are recorded in history
- Test history queries and filtering

### 7. Search and Filtering
- Test searching by company, industry, location
- Test filtering by status, value range, lead source

### 8. Conversion Analysis
- Analyze conversion paths (NEW → CONVERTED)
- Test conversion rate calculations

### 9. Rejection Handling
- Test rejection workflow and reasons
- Verify rejected leads don't appear in active lists

### 10. Time-Based Queries
- Test queries for recent leads (last 7 days, 30 days)
- Test lead aging (how long in current status)

## Notes

- All passwords are hashed using BCrypt (placeholder hashes shown)
- Email addresses follow the pattern: `firstname.lastname@company.com` or `contact@company.com`
- Phone numbers use format: `XXX-555-XXXX`
- Dates are calculated relative to CURRENT_TIMESTAMP using INTERVAL for realistic time distribution
- Lead scores correlate with potential value but include some variance for realism
- Some leads have NULL assigned_to (NEW status)
- One user (Lucas Lopez) is inactive to test edge cases

## Customization

You can modify the SQL script to:
- Adjust the number of leads (currently 100)
- Change the value distribution
- Modify status distribution
- Add more users or change user roles
- Add more lead history entries
- Change date ranges for realistic time distribution

## Maintenance

If you need to regenerate or update the data:
1. Clear existing data (see comments in SQL file)
2. Run the synthetic-data.sql script again
3. Verify data using the queries above

