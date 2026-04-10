-- Seed events for the 6 locations that were missing them: Redwood City, Menlo Park,
-- San Mateo, San Bruno, South San Francisco, and San Francisco.
DO $$
DECLARE
    RedwoodCity UUID;
    MenloPark UUID;
    SanMateo UUID;
    SanBruno UUID;
    SouthSanFrancisco UUID;
    SanFrancisco UUID;

    TechLayoffs UUID;
    OpenSourceContribution UUID;
    OfficeSpaceCrisis UUID;
    AcquisitionOffer UUID;
    SurgeInUserSignups UUID;
    OutageDuringDemo UUID;
    CompetitorLaunch UUID;
    TalentPoaching UUID;
    SeriesAFunding UUID;
    ViralBlogPost UUID;
    DataPrivacyAudit UUID;
    PlatformOutage UUID;

BEGIN
    RedwoodCity        := (SELECT id FROM locations WHERE city = 'Redwood City'        AND state = 'CA');
    MenloPark          := (SELECT id FROM locations WHERE city = 'Menlo Park'          AND state = 'CA');
    SanMateo           := (SELECT id FROM locations WHERE city = 'San Mateo'           AND state = 'CA');
    SanBruno           := (SELECT id FROM locations WHERE city = 'San Bruno'           AND state = 'CA');
    SouthSanFrancisco  := (SELECT id FROM locations WHERE city = 'South San Francisco' AND state = 'CA');
    SanFrancisco       := (SELECT id FROM locations WHERE city = 'San Francisco'       AND state = 'CA');

    INSERT INTO events (title, description, location_id, event_source, weight) VALUES
    ('Tech Layoffs Nearby',        'A wave of layoffs hits the local tech scene, rattling morale across the industry.', RedwoodCity, 'GENERIC', 4),
    ('Open Source Contribution',   'Your side project goes viral on GitHub after an Oracle engineer stars it.', RedwoodCity, 'GENERIC', 3),
    ('Office Space Crisis',        'Your Menlo Park co-working contract is up — rents have doubled.', MenloPark, 'GENERIC', 4),
    ('Acquisition Offer',          'A larger company has made a preliminary offer to acquire your startup.', MenloPark, 'GENERIC', 3),
    ('Surge in User Signups',      'A viral tweet drove a 10x spike in signups overnight — servers are struggling.', SanMateo, 'GENERIC', 3),
    ('Outage During a Demo',       'Your app goes down mid-demo in front of a key enterprise client.', SanMateo, 'GENERIC', 5),
    ('Competitor Launch',          'A well-funded competitor just launched a product nearly identical to yours.', SanBruno, 'GENERIC', 4),
    ('Talent Poaching',            'Google recruiters have approached two of your best engineers.', SanBruno, 'GENERIC', 4),
    ('Series A Funding Round',     'VCs in the Bay are buzzing — a firm wants to lead your Series A.', SouthSanFrancisco, 'GENERIC', 2),
    ('Data Privacy Audit',         'A regulatory body has initiated a data privacy audit of your platform.', SouthSanFrancisco, 'GENERIC', 4),
    ('Viral Blog Post',            'A glowing blog post about your product is trending on Hacker News.', SanFrancisco, 'GENERIC', 2),
    ('Platform Outage',            'Your cloud provider suffered a major outage, taking your Salesforce integration offline.', SanFrancisco, 'GENERIC', 5);

    TechLayoffs               := (SELECT id FROM events WHERE title = 'Tech Layoffs Nearby');
    OpenSourceContribution    := (SELECT id FROM events WHERE title = 'Open Source Contribution');
    OfficeSpaceCrisis         := (SELECT id FROM events WHERE title = 'Office Space Crisis');
    AcquisitionOffer          := (SELECT id FROM events WHERE title = 'Acquisition Offer');
    SurgeInUserSignups        := (SELECT id FROM events WHERE title = 'Surge in User Signups');
    OutageDuringDemo          := (SELECT id FROM events WHERE title = 'Outage During a Demo');
    CompetitorLaunch          := (SELECT id FROM events WHERE title = 'Competitor Launch');
    TalentPoaching            := (SELECT id FROM events WHERE title = 'Talent Poaching');
    SeriesAFunding            := (SELECT id FROM events WHERE title = 'Series A Funding Round');
    DataPrivacyAudit          := (SELECT id FROM events WHERE title = 'Data Privacy Audit');
    ViralBlogPost             := (SELECT id FROM events WHERE title = 'Viral Blog Post');
    PlatformOutage            := (SELECT id FROM events WHERE title = 'Platform Outage');

    INSERT INTO event_choices (event_id, description, cash_delta, morale_delta, hype_delta, bug_count_delta, coffee_supply_delta) VALUES
    -- Tech Layoffs Nearby
    (TechLayoffs, 'Hire laid-off engineers at below-market rates to grow your team',  300, 10,  10, 0,  0),
    (TechLayoffs, 'Boost morale by publicly pledging no layoffs at your company',       0, 20,  15, 0,  0),
    (TechLayoffs, 'Do nothing and watch morale slide',                                  0, -15, -10, 0,  0),
    -- Open Source Contribution
    (OpenSourceContribution, 'Invest time to maintain the project and build community',  -100, 10, 30, 0, 0),
    (OpenSourceContribution, 'Hand off the project and focus on the product',              0,  0, 10, 0, 0),
    (OpenSourceContribution, 'Abandon the project — no time to maintain it',               0, -10, -20, 5, 0),
    -- Office Space Crisis
    (OfficeSpaceCrisis, 'Sign a new lease at the higher rate',                          -500, 5,   0, 0,  0),
    (OfficeSpaceCrisis, 'Go fully remote and cut the office entirely',                     0, -10, -5, 0,  5),
    (OfficeSpaceCrisis, 'Negotiate a short-term sublease from a neighbour',             -200, 5,   5, 0,  0),
    -- Acquisition Offer
    (AcquisitionOffer, 'Enter negotiations and explore the offer seriously',               0, 10, 20, 0,  0),
    (AcquisitionOffer, 'Decline and double down on independence',                          0, 15, 10, 0,  0),
    (AcquisitionOffer, 'Counter with an inflated valuation to test their resolve',         0,  5, 15, 0,  0),
    -- Surge in User Signups
    (SurgeInUserSignups, 'Quickly scale infrastructure to handle the load',             -400, 10, 20, 0,  0),
    (SurgeInUserSignups, 'Implement a waitlist to throttle signups temporarily',            0,  5, 10, 0,  0),
    (SurgeInUserSignups, 'Do nothing — the site crashes for new users',                    0, -20, -30, 5, 0),
    -- Outage During Demo
    (OutageDuringDemo, 'Apologise transparently and offer a follow-up demo',            -100, -10, -10, 0, 0),
    (OutageDuringDemo, 'Blame it on the cloud provider and move on',                       0, -20, -20, 0, 0),
    (OutageDuringDemo, 'Offer the client a free trial to recover trust',               -200,  10,  10, 0, 0),
    -- Competitor Launch
    (CompetitorLaunch, 'Accelerate your roadmap and ship a differentiating feature',   -300, -10,  20, 5,  0),
    (CompetitorLaunch, 'Launch a competitive pricing campaign',                         -200,   5,  15, 0,  0),
    (CompetitorLaunch, 'Ignore it and stay focused',                                       0,   0, -10, 0,  0),
    -- Talent Poaching
    (TalentPoaching, 'Offer retention bonuses to keep your engineers',                  -400,  20,  10, 0,  0),
    (TalentPoaching, 'Let them go but improve hiring pipeline',                             0, -15,  -5, 0,  0),
    (TalentPoaching, 'Have a frank conversation about growth and mission',                  0,  15,   5, 0,  0),
    -- Series A Funding Round
    (SeriesAFunding, 'Accept the term sheet and close the round',                        1000, 20,  30, 0,  0),
    (SeriesAFunding, 'Run a competitive process with multiple VCs',                        200, 10,  20, 0, -5),
    (SeriesAFunding, 'Decline — you prefer to stay bootstrapped',                            0, -5,  -10, 0, 0),
    -- Data Privacy Audit
    (DataPrivacyAudit, 'Cooperate fully and implement all recommendations',              -300, -10,  10, 0, -5),
    (DataPrivacyAudit, 'Hire a specialist firm to manage the audit response',            -200,   5,   5, 0,  0),
    (DataPrivacyAudit, 'Delay and provide minimal documentation',                            0, -25, -20, 0, 0),
    -- Viral Blog Post
    (ViralBlogPost, 'Engage with the community and ride the wave',                          0,  20,  30, 0,  0),
    (ViralBlogPost, 'Turn it into a marketing campaign',                                 -100,  10,  20, 0,  0),
    (ViralBlogPost, 'Do nothing and let it fade',                                            0,   0,  -5, 0,  0),
    -- Platform Outage
    (PlatformOutage, 'Work around the clock to restore service',                          -100, -20, -10, 0, -10),
    (PlatformOutage, 'Migrate critical workloads to a backup provider',                   -400,  -5,   5, 0,   0),
    (PlatformOutage, 'Communicate proactively with customers while waiting for recovery',    0, -10,  10, 0,   0);

END $$;
