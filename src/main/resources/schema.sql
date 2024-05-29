CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name character varying(255)  NOT NULL,
    email character varying(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    description character varying(512) NOT NULL,
    requester_id bigint NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_requests PRIMARY KEY (id),
        CONSTRAINT fk_requests_users FOREIGN KEY (requester_id)
            REFERENCES public.users (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name character varying(255) NOT NULL,
    description character varying(512) NOT NULL,
    is_available boolean NOT NULL,
    owner_id bigint NOT NULL,
    request_id bigint,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_items_users FOREIGN KEY (owner_id)
        REFERENCES public.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_items_requests FOREIGN KEY (request_id)
        REFERENCES public.requests (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    start_time TIMESTAMP WITHOUT TIME ZONE,
    end_time TIMESTAMP WITHOUT TIME ZONE,
    item_id bigint NOT NULL,
    booker_id bigint NOT NULL,
    status character varying(10),
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_bookings_users FOREIGN KEY (booker_id)
        REFERENCES public.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_bookings_items FOREIGN KEY (item_id)
        REFERENCES public.items (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS comments
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    text character varying(1024)  NOT NULL,
    item_id bigint NOT NULL,
    author_id bigint NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_users FOREIGN KEY (author_id)
        REFERENCES public.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_comments_items FOREIGN KEY (item_id)
        REFERENCES public.items (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
