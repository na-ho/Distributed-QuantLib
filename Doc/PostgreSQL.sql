CREATE TABLE public."EuropeanOptionMCSobolDataParam" (
	id int4 NOT NULL DEFAULT nextval('europeanoptionmcsoboldataparam_id_seq'::regclass),
	optiontype int4 NOT NULL,
	evaluationdate_year int4 NOT NULL,
	evaluationdate_month int4 NOT NULL,
	evaluationdate_day int4 NOT NULL,
	settlementdate_year int4 NOT NULL,
	settlementdate_month int4 NOT NULL,
	settlementdate_day int4 NULL,
	maturitydate_year int4 NOT NULL,
	maturitydate_month int4 NOT NULL,
	maturitydate_day int4 NOT NULL,
	riskfreerate float8 NOT NULL,
	dividendyield float8 NOT NULL,
	volatility float8 NOT NULL,
	strike float8 NOT NULL,
	underlying float8 NOT NULL,
	timesteps int4 NOT NULL,
	nsamples int4 NOT NULL,
	CONSTRAINT europeanoptionmcsoboldataparam_pk PRIMARY KEY (id)
);