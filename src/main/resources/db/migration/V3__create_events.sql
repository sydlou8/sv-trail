CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    location_id UUID REFERENCES locations(id) ON DELETE SET NULL,
    event_source VARCHAR(255) NOT NULL DEFAULT 'GENERIC',
    weight INT NOT NULL DEFAULT 1,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CHECK (weight >= 1)
);

CREATE TABLE event_choices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    description VARCHAR(255) NOT NULL,
    cash_delta INT NOT NULL DEFAULT 0,
    morale_delta INT NOT NULL DEFAULT 0,
    hype_delta INT NOT NULL DEFAULT 0,
    bug_count_delta INT NOT NULL DEFAULT 0,
    coffee_supply_delta INT NOT NULL DEFAULT 0
);