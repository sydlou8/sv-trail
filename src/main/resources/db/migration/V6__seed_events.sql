-- must wrap in DO block to use ":=" to set variables
DO $$
DECLARE
    -- Locations
    SanJose UUID;
    Cupertino UUID;
    SantaClara UUID;
    Sunnyvale UUID;
    MountainView UUID;
    PaloAlto UUID;
    RedwoodCity UUID;
    MenloPark UUID;
    SanMateo UUID;
    SanBruno UUID;
    SouthSanFrancisco UUID;
    SanFrancisco UUID;

    -- Events
    ProductionIsDown UUID;
    NewFeatureRequest UUID;
    SecurityBreach UUID;
    PerformanceBottleneck UUID;
    DataLossIncident UUID;
    UnexpectedServerCosts UUID;
    KeyEmployeeResignation UUID;
    MajorBugFound UUID;
    CustomerChurnSpike UUID;
    PositiveMediaCoverage UUID;
    InvestorInterest UUID;
    RegulatoryChangeImpactingBusiness UUID;

BEGIN
    -- Set each location id into a variable
    SanJose := (SELECT id FROM locations WHERE city = 'San Jose' AND state = 'CA');
    Cupertino := (SELECT id FROM locations WHERE city = 'Cupertino' AND state = 'CA');
    SantaClara := (SELECT id FROM locations WHERE city = 'Santa Clara' AND state = 'CA');
    Sunnyvale := (SELECT id FROM locations WHERE city = 'Sunnyvale' AND state = 'CA');
    MountainView := (SELECT id FROM locations WHERE city = 'Mountain View' AND state = 'CA');
    PaloAlto := (SELECT id FROM locations WHERE city = 'Palo Alto' AND state = 'CA');
    RedwoodCity := (SELECT id FROM locations WHERE city = 'Redwood City' AND state = 'CA');
    MenloPark := (SELECT id FROM locations WHERE city = 'Menlo Park' AND state = 'CA');
    SanMateo := (SELECT id FROM locations WHERE city = 'San Mateo' AND state = 'CA');
    SanBruno := (SELECT id FROM locations WHERE city = 'San Bruno' AND state = 'CA');
    SouthSanFrancisco := (SELECT id FROM locations WHERE city = 'South San Francisco' AND state = 'CA');
    SanFrancisco := (SELECT id FROM locations WHERE city = 'San Francisco' AND state = 'CA');

    -- Insert events for each location. 2 events per location
    -- Note: event_source is used to categorize events based on their source (e.g. GENERIC for seeeded events, WEATHER for weather api events, GITHUB for github api events, etc).
    INSERT INTO events (title, description, location_id, event_source, weight) VALUES
    ('Production is Down', 'Your app has crashed at 2am, All hands on deck', SanJose, 'GENERIC', 5),
    ('New Feature Request', 'A major client has requested a new feature that could be a game-changer.', SanJose, 'GENERIC', 3),
    ('Security Breach', 'A security vulnerability has been discovered in your application.', Cupertino, 'GENERIC', 5),
    ('Performance Bottleneck', 'Users are reporting slow performance during peak hours.', Cupertino, 'GENERIC', 4),
    ('Data Loss Incident', 'A critical data loss incident has occurred due to a backup failure.', SantaClara, 'GENERIC', 5),
    ('Unexpected Server Costs', 'Your cloud provider has unexpectedly increased server costs this month.', SantaClara, 'GENERIC', 3),
    ('Key Employee Resignation', 'Your lead developer has resigned unexpectedly.', Sunnyvale, 'GENERIC', 4),
    ('Major Bug Found', 'A major bug has been found in the latest release, causing crashes for many users.', Sunnyvale, 'GENERIC', 5),
    ('Customer Churn Spike', 'You have noticed a sudden spike in customer churn rates.', MountainView, 'GENERIC', 4),
    ('Positive Media Coverage', 'Your company received positive media coverage for an innovative project.', MountainView, 'GENERIC', 2),
    ('Investor Interest', 'A prominent investor has expressed interest in funding your startup.', PaloAlto, 'GENERIC', 3),
    ('Regulatory Change Impacting Business', 'New regulations have been introduced that could impact your business operations.', PaloAlto, 'GENERIC', 4);

    -- Set each event id into a variable must be done after events are inserted
    ProductionIsDown := (SELECT id FROM events WHERE title = 'Production is Down');
    NewFeatureRequest := (SELECT id FROM events WHERE title = 'New Feature Request');
    SecurityBreach := (SELECT id FROM events WHERE title = 'Security Breach');
    PerformanceBottleneck := (SELECT id FROM events WHERE title = 'Performance Bottleneck');
    DataLossIncident := (SELECT id FROM events WHERE title = 'Data Loss Incident');
    UnexpectedServerCosts := (SELECT id FROM events WHERE title = 'Unexpected Server Costs');
    KeyEmployeeResignation := (SELECT id FROM events WHERE title = 'Key Employee Resignation');
    MajorBugFound := (SELECT id FROM events WHERE title = 'Major Bug Found');
    CustomerChurnSpike := (SELECT id FROM events WHERE title = 'Customer Churn Spike');
    PositiveMediaCoverage := (SELECT id FROM events WHERE title = 'Positive Media Coverage');
    InvestorInterest := (SELECT id FROM events WHERE title = 'Investor Interest');
    RegulatoryChangeImpactingBusiness := (SELECT id FROM events WHERE title = 'Regulatory Change Impacting Business');

    -- Insert choices for each event. 3 choices per event
    INSERT INTO event_choices (event_id, description, cash_delta, morale_delta, hype_delta, bug_count_delta, coffee_supply_delta) VALUES
    (ProductionIsDown, 'Work through the night to fix the issue', -150, -20, -10, 0, -10),
    (ProductionIsDown, 'Pay for emergency support from cloud provider', -400, -10, -5, 0, 0),
    (ProductionIsDown, 'Ignore and hope it resolves itself', 0, -50, -30, 0, 0),
    (NewFeatureRequest, 'Accept the request and prioritize it in the roadmap', 500, 20, 30, 0, 0),
    (NewFeatureRequest, 'Politely decline the request and explain why it doesn''t fit the product vision', 0, -10, -20, 0, 0),
    (NewFeatureRequest, 'Accept the request but outsource the work to a cheaper contractor', 250, -10, 10, 0, 0),
    (SecurityBreach, 'Immediately patch the vulnerability and conduct a security audit', -300, -20, 10, 0, -5),
    (SecurityBreach, 'Publicly disclose the vulnerability and its potential impact', -300, -10, 20, 0, 0),
    (SecurityBreach, 'Downplay the issue and assure customers it''s being handled', -200, -30, -20, 0, 0),
    (PerformanceBottleneck, 'Invest in performance monitoring tools and optimize code', -300, 10, 20, 0, -5),
    (PerformanceBottleneck, 'Scale up infrastructure to handle increased load', -400, 0, 10, 0, 0),
    (PerformanceBottleneck, 'Do nothing and hope users don''t notice', 0, -20, -30, 0, 0),
    (DataLossIncident, 'Offer affected customers compensation and work to restore data', -400, -30, 10, 0, -10),
    (DataLossIncident, 'Conduct a thorough investigation and implement stronger backup procedures', -300, -10, 20, 0, -5),
    (DataLossIncident, 'Downplay the incident and assure customers it won''t happen again', -200, -20, -10, 0, 0),
    (UnexpectedServerCosts, 'Optimize server usage and reduce costs', -300, 0, 10, 0, 0),
    (UnexpectedServerCosts, 'Negotiate with cloud provider for better rates', -100, 0, 5, 0, 0),
    (UnexpectedServerCosts, 'Ignore the issue and hope costs go down next month', 0, -10, -20, 0, 0),
    (KeyEmployeeResignation, 'Offer a counteroffer to try to retain the employee', -300, 10, 0, 0, 0),
    (KeyEmployeeResignation, 'Conduct an exit interview to understand reasons for leaving and improve company culture', 0, 5, 0, 0, 0),
    (KeyEmployeeResignation, 'Accept the resignation and start recruiting for a replacement', 0, -5, 0, 0, 0),
    (MajorBugFound, 'Roll back to the previous stable release and fix the bug', -300, -10, -20, 0, 0),
    (MajorBugFound, 'Issue a patch to fix the bug as quickly as possible', -300, -5, 10, 0, -5),
    (MajorBugFound, 'Downplay the bug and assure customers it''s being handled', -100, -20, -30, 0, 0),
    (CustomerChurnSpike, 'Launch a customer retention campaign with special offers and improved support', -300, 20, 30, 0, 0),
    (CustomerChurnSpike, 'Conduct customer surveys to understand reasons for churn and improve the product', -200, 10, 20, 0, 0),
    (CustomerChurnSpike, 'Ignore the issue and hope churn rates go down next month', 0, -20, -30, 0, 0),
    (PositiveMediaCoverage, 'Amplify the coverage on social media and in marketing materials', -100, 10, 20, 0, 0),
    (PositiveMediaCoverage, 'Host a celebratory event for employees and customers to capitalize on the positive momentum', -300, 20, 30, 0, 0),
    (PositiveMediaCoverage, 'Do nothing and hope the positive coverage continues', 0, -10, -20, 0, 0),
    (InvestorInterest, 'Engage in discussions with the investor to explore funding opportunities', 0, 10, 20, 0, 0),
    (InvestorInterest, 'Politely decline the investor''s interest to maintain independence', 0, -10, -20, 0, 0),
    (InvestorInterest, 'Accept the investor''s interest and start the funding process', 500, 20, 30, 0, 0),
    (RegulatoryChangeImpactingBusiness, 'Adapt business operations to comply with new regulations', -150, 0, 10, 0, 0),
    (RegulatoryChangeImpactingBusiness, 'Lobby for changes to the regulations that are more favorable to your business', -100, 0, 5, 0, 0),
    (RegulatoryChangeImpactingBusiness, 'Ignore the regulations and risk potential fines and legal issues', 0, -20, -30, 0, 0);     
END $$;