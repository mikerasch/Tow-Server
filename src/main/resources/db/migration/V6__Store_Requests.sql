CREATE TABLE public.requests
(
    request_id uuid DEFAULT gen_random_uuid() NOT NULL,
    driver_id  uuid NOT NULL,
    tow_truck_id uuid NOT NULL,
    request_date timestamp NOT NULL,

    CONSTRAINT request_pkey PRIMARY KEY (request_id)
);
