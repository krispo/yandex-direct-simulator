# --- !Ups


CREATE TABLE "ActualBidHistory" (
    id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    bid double precision NOT NULL,
    bannerphrase_id bigint NOT NULL
);


ALTER TABLE "ActualBidHistory" OWNER TO postgres;


CREATE TABLE "Banner" (
    text character varying(128) NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE "Banner" OWNER TO postgres;


CREATE TABLE "BannerPhrase" (
    banner_id bigint NOT NULL,
    region_id bigint NOT NULL,
    id bigint NOT NULL,
    phrase_id bigint NOT NULL,
    campaign_id bigint NOT NULL
);


ALTER TABLE "BannerPhrase" OWNER TO postgres;


CREATE TABLE "BannerPhrasePerformance" (
    cost_search double precision NOT NULL,
    impress_context integer NOT NULL,
    impress_search integer NOT NULL,
    clicks_context integer NOT NULL,
    clicks_search integer NOT NULL,
    cost_context double precision NOT NULL,
    id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    periodtype_id bigint NOT NULL,
    bannerphrase_id bigint NOT NULL
);


ALTER TABLE "BannerPhrasePerformance" OWNER TO postgres;


CREATE TABLE "BudgetHistory" (
    id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    budget double precision NOT NULL,
    campaign_id bigint NOT NULL
);


ALTER TABLE "BudgetHistory" OWNER TO postgres;


CREATE TABLE "Campaign" (
    _login character varying(128) NOT NULL,
    _token character varying(128) NOT NULL,
    name character varying(128) NOT NULL,
    user_id bigint NOT NULL,
    id bigint NOT NULL,
    start timestamp without time zone NOT NULL
);


ALTER TABLE "Campaign" OWNER TO postgres;


CREATE TABLE "CampaignPerformance" (
    cost_search double precision NOT NULL,
    impress_context integer NOT NULL,
    impress_search integer NOT NULL,
    clicks_context integer NOT NULL,
    clicks_search integer NOT NULL,
    cost_context double precision NOT NULL,
    id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    periodtype_id bigint NOT NULL,
    campaign_id bigint NOT NULL
);


ALTER TABLE "CampaignPerformance" OWNER TO postgres;


CREATE TABLE "NetAdvisedBidHistory" (
    e double precision NOT NULL,
    f double precision NOT NULL,
    a double precision NOT NULL,
    id bigint NOT NULL,
    b double precision NOT NULL,
    date timestamp without time zone NOT NULL,
    c double precision NOT NULL,
    bannerphrase_id bigint NOT NULL,
    d double precision NOT NULL
);


ALTER TABLE "NetAdvisedBidHistory" OWNER TO postgres;


CREATE TABLE "Phrase" (
    phrase character varying(512) NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE "Phrase" OWNER TO postgres;


CREATE TABLE "Region" (
    parent_id bigint NOT NULL,
    description character varying(128) NOT NULL,
    region_id bigint NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE "Region" OWNER TO postgres;


CREATE TABLE ad_user (
    name character varying(128) NOT NULL,
    id bigint NOT NULL,
    password character varying(128) NOT NULL
);


ALTER TABLE ad_user OWNER TO postgres;


CREATE SEQUENCE "s_ActualBidHistory_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_ActualBidHistory_id" OWNER TO postgres;


CREATE SEQUENCE "s_BannerPhrasePerformance_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_BannerPhrasePerformance_id" OWNER TO postgres;


CREATE SEQUENCE "s_BannerPhrase_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_BannerPhrase_id" OWNER TO postgres;


CREATE SEQUENCE "s_Banner_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_Banner_id" OWNER TO postgres;


CREATE SEQUENCE "s_BudgetHistory_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_BudgetHistory_id" OWNER TO postgres;


CREATE SEQUENCE "s_CampaignPerformance_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_CampaignPerformance_id" OWNER TO postgres;


CREATE SEQUENCE "s_Campaign_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_Campaign_id" OWNER TO postgres;


CREATE SEQUENCE "s_NetAdvisedBidHistory_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_NetAdvisedBidHistory_id" OWNER TO postgres;


CREATE SEQUENCE "s_Phrase_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_Phrase_id" OWNER TO postgres;


CREATE SEQUENCE "s_Region_id"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "s_Region_id" OWNER TO postgres;


CREATE SEQUENCE s_ad_user_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE s_ad_user_id OWNER TO postgres;


ALTER TABLE ONLY "ActualBidHistory"
    ADD CONSTRAINT "ActualBidHistory_pkey" PRIMARY KEY (id);



ALTER TABLE ONLY "BannerPhrasePerformance"
    ADD CONSTRAINT "BannerPhrasePerformance_pkey" PRIMARY KEY (id);



ALTER TABLE ONLY "BannerPhrase"
    ADD CONSTRAINT "BannerPhrase_pkey" PRIMARY KEY (id);



ALTER TABLE ONLY "Banner"
    ADD CONSTRAINT "Banner_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY "BudgetHistory"
    ADD CONSTRAINT "BudgetHistory_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY "CampaignPerformance"
    ADD CONSTRAINT "CampaignPerformance_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY "Campaign"
    ADD CONSTRAINT "Campaign_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY "NetAdvisedBidHistory"
    ADD CONSTRAINT "NetAdvisedBidHistory_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY "Phrase"
    ADD CONSTRAINT "Phrase_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY "Region"
    ADD CONSTRAINT "Region_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY ad_user
    ADD CONSTRAINT ad_user_pkey PRIMARY KEY (id);

CREATE UNIQUE INDEX idx1eb904b2 ON ad_user USING btree (name);


ALTER TABLE ONLY "ActualBidHistory"
    ADD CONSTRAINT "ActualBidHistoryFK9" FOREIGN KEY (bannerphrase_id) REFERENCES "BannerPhrase"(id);

ALTER TABLE ONLY "BannerPhrase"
    ADD CONSTRAINT "BannerPhraseFK4" FOREIGN KEY (campaign_id) REFERENCES "Campaign"(id);

ALTER TABLE ONLY "BannerPhrase"
    ADD CONSTRAINT "BannerPhraseFK5" FOREIGN KEY (banner_id) REFERENCES "Banner"(id);


ALTER TABLE ONLY "BannerPhrase"
    ADD CONSTRAINT "BannerPhraseFK6" FOREIGN KEY (phrase_id) REFERENCES "Phrase"(id);


ALTER TABLE ONLY "BannerPhrase"
    ADD CONSTRAINT "BannerPhraseFK7" FOREIGN KEY (region_id) REFERENCES "Region"(id);


ALTER TABLE ONLY "BannerPhrasePerformance"
    ADD CONSTRAINT "BannerPhrasePerformanceFK8" FOREIGN KEY (bannerphrase_id) REFERENCES "BannerPhrase"(id);

ALTER TABLE ONLY "BudgetHistory"
    ADD CONSTRAINT "BudgetHistoryFK3" FOREIGN KEY (campaign_id) REFERENCES "Campaign"(id);

ALTER TABLE ONLY "Campaign"
    ADD CONSTRAINT "CampaignFK1" FOREIGN KEY (user_id) REFERENCES ad_user(id);


ALTER TABLE ONLY "CampaignPerformance"
    ADD CONSTRAINT "CampaignPerformanceFK2" FOREIGN KEY (campaign_id) REFERENCES "Campaign"(id);


ALTER TABLE ONLY "NetAdvisedBidHistory"
    ADD CONSTRAINT "NetAdvisedBidHistoryFK10" FOREIGN KEY (bannerphrase_id) REFERENCES "BannerPhrase"(id);


# --- !Downs

ALTER TABLE ONLY "NetAdvisedBidHistory" DROP CONSTRAINT "NetAdvisedBidHistoryFK10";
ALTER TABLE ONLY "CampaignPerformance" DROP CONSTRAINT "CampaignPerformanceFK2";
ALTER TABLE ONLY "Campaign" DROP CONSTRAINT "CampaignFK1";
ALTER TABLE ONLY "BudgetHistory" DROP CONSTRAINT "BudgetHistoryFK3";
ALTER TABLE ONLY "BannerPhrasePerformance" DROP CONSTRAINT "BannerPhrasePerformanceFK8";
ALTER TABLE ONLY "BannerPhrase" DROP CONSTRAINT "BannerPhraseFK7";
ALTER TABLE ONLY "BannerPhrase" DROP CONSTRAINT "BannerPhraseFK6";
ALTER TABLE ONLY "BannerPhrase" DROP CONSTRAINT "BannerPhraseFK5";
ALTER TABLE ONLY "BannerPhrase" DROP CONSTRAINT "BannerPhraseFK4";
ALTER TABLE ONLY "ActualBidHistory" DROP CONSTRAINT "ActualBidHistoryFK9";

ALTER TABLE ONLY ad_user DROP CONSTRAINT ad_user_pkey;
ALTER TABLE ONLY "Region" DROP CONSTRAINT "Region_pkey";
ALTER TABLE ONLY "Phrase" DROP CONSTRAINT "Phrase_pkey";
ALTER TABLE ONLY "NetAdvisedBidHistory" DROP CONSTRAINT "NetAdvisedBidHistory_pkey";
ALTER TABLE ONLY "Campaign" DROP CONSTRAINT "Campaign_pkey";
ALTER TABLE ONLY "CampaignPerformance" DROP CONSTRAINT "CampaignPerformance_pkey";
ALTER TABLE ONLY "BudgetHistory" DROP CONSTRAINT "BudgetHistory_pkey";
ALTER TABLE ONLY "Banner" DROP CONSTRAINT "Banner_pkey";
ALTER TABLE ONLY "BannerPhrase" DROP CONSTRAINT "BannerPhrase_pkey";
ALTER TABLE ONLY "BannerPhrasePerformance" DROP CONSTRAINT "BannerPhrasePerformance_pkey";
ALTER TABLE ONLY "ActualBidHistory" DROP CONSTRAINT "ActualBidHistory_pkey";

DROP SEQUENCE s_ad_user_id;
DROP SEQUENCE "s_Region_id";
DROP SEQUENCE "s_Phrase_id";
DROP SEQUENCE "s_NetAdvisedBidHistory_id";
DROP SEQUENCE "s_Campaign_id";
DROP SEQUENCE "s_CampaignPerformance_id";
DROP SEQUENCE "s_BudgetHistory_id";
DROP SEQUENCE "s_Banner_id";
DROP SEQUENCE "s_BannerPhrase_id";
DROP SEQUENCE "s_BannerPhrasePerformance_id";
DROP SEQUENCE "s_ActualBidHistory_id";

DROP TABLE ad_user;
DROP TABLE "Region";
DROP TABLE "Phrase";
DROP TABLE "NetAdvisedBidHistory";
DROP TABLE "CampaignPerformance";
DROP TABLE "Campaign";
DROP TABLE "BudgetHistory";
DROP TABLE "BannerPhrasePerformance";
DROP TABLE "BannerPhrase";
DROP TABLE "Banner";
DROP TABLE "ActualBidHistory";
