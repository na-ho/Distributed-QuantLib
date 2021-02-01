package com.na_ho.util;

public class MsgConst {

    public static final int NUM_PARTITIONS_SHARE = 5;
    public static final String TOPIC_NAME_REPLY = "data-reply";

    public static final String TOPIC_NAME_BOND_FIXED_RATE = "pricer-BondFixedRate";
    public static final int NUM_PARTITIONS_BOND_FIXED_RATE = NUM_PARTITIONS_SHARE;
    public static final String TOPIC_NAME_BOND_FIXED_RATE_RESULT = "pricer-BondFixedRateResult";
    public static final String TOPIC_NAME_BOND_FIXED_RATE_DATA_REQUEST_CALCULATION_ID = "data-BondFixedRateData-RequestCalculationID";
    public static final String TOPIC_NAME_BOND_FIXED_RATE_RESULT_GET = "data-BondFixedRateResult-get";

    public static final String TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL = "pricer-EuropeanOptionMCSobol";
    public static final int NUM_PARTITIONS_EUROPEAN_OPTION_MC_SOBOL = NUM_PARTITIONS_SHARE;
    public static final String TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_RESULT = "pricer-EuropeanOptionMCSobolResult";
    public static final String TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_REQUEST_CALCULATION_ID = "data-EuropeanOptionMCSobol-RequestCalculationID";
    public static final String TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET = "data-EuropeanOptionMCSobol-get";
    public static final String TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_ADD_DATA = "position-EuropeanOptionMCSobol-addData";
    public static final String TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET_DATA = "position-EuropeanOptionMCSobol-getData";

    public static final String TOPIC_NAME_POSITION_CALCULATE_ALL = "position-calculate-all";
    public static final String TOPIC_NAME_POSITION_CALCULATE_DELTA = "position-calculate-delta";
    public static final String TOPIC_NAME_POSITION_CALCULATE_THETA = "position-calculate-theta";
    public static final String TOPIC_NAME_POSITION_CALCULATE_VEGA = "position-calculate-vega";

    public static final String TOPIC_NAME_POSITION_CALCULATE_VEGA_REQUEST_CALCULATION_ID = "position-calculate-vega-requestCalculationID";
    public static final String TOPIC_NAME_POSITION_CALCULATE_VEGA_SAVE = "position-calculate-vega-save";
    public static final String TOPIC_NAME_POSITION_CALCULATE_VEGA_GET = "position-calculate-vega-get";

}
