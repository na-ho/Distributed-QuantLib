#include <jni.h>        // JNI header provided by JDK
//#include <stdio.h>      // C Standard IO Header

#include <ql/qldefines.hpp>
#ifdef BOOST_MSVC
#  include <ql/auto_link.hpp>
#endif
#include <ql/instruments/bonds/zerocouponbond.hpp>
#include <ql/instruments/bonds/floatingratebond.hpp>
#include <ql/pricingengines/bond/discountingbondengine.hpp>
#include <ql/cashflows/couponpricer.hpp>
#include <ql/termstructures/yield/piecewiseyieldcurve.hpp>
#include <ql/termstructures/yield/bondhelpers.hpp>
#include <ql/termstructures/volatility/optionlet/constantoptionletvol.hpp>
#include <ql/indexes/ibor/euribor.hpp>
#include <ql/indexes/ibor/usdlibor.hpp>
#include <ql/time/calendars/target.hpp>
#include <ql/time/calendars/unitedstates.hpp>
#include <ql/time/daycounters/actualactual.hpp>
#include <ql/time/daycounters/actual360.hpp>
#include <ql/time/daycounters/thirty360.hpp>

#include <ql/instruments/vanillaoption.hpp>
#include <ql/pricingengines/vanilla/binomialengine.hpp>
#include <ql/pricingengines/vanilla/analyticeuropeanengine.hpp>
#include <ql/pricingengines/vanilla/analytichestonengine.hpp>
#include <ql/pricingengines/vanilla/baroneadesiwhaleyengine.hpp>
#include <ql/pricingengines/vanilla/bjerksundstenslandengine.hpp>
#include <ql/pricingengines/vanilla/batesengine.hpp>
#include <ql/pricingengines/vanilla/integralengine.hpp>
#include <ql/pricingengines/vanilla/fdblackscholesvanillaengine.hpp>
#include <ql/pricingengines/vanilla/mceuropeanengine.hpp>
#include <ql/pricingengines/vanilla/mcamericanengine.hpp>
#include <ql/pricingengines/vanilla/analyticeuropeanvasicekengine.hpp>
#include <ql/time/calendars/target.hpp>
#include <ql/utilities/dataformatters.hpp>
#include <ql/models/shortrate/onefactormodels/vasicek.hpp>

//#include "HelloJNI.h"   // Generated
#include "com_na_ho_pricer_jni_QuantJNI.h"

using namespace QuantLib;


JNIEXPORT void JNICALL Java_com_na_1ho_pricer_jni_QuantJNI_sayHello(JNIEnv* env, jobject thisObj) {
	printf("Hello Distributed QuantLib!\n");
	return;
}

JNIEXPORT jdoubleArray JNICALL Java_com_na_1ho_pricer_jni_QuantJNI_priceFixedRateBond(JNIEnv* env, jobject thisObject
	, jint settlementDate_year, jint settlementDate_month, jint settlementDate_day
	, jint fixingDays, jint settlementDays, jdouble zc3mQuote, jdouble zc6mQuote, jdouble zc1yQuote, jdouble redemption
	, jint numberOfBonds, jintArray issueDates_year, jintArray issueDates_month, jintArray issueDates_day
	, jintArray maturities_year, jintArray maturities_month, jintArray maturities_day
	, jdoubleArray couponRates, jdoubleArray marketQuotes
	, jint effectiveDate_year, jint effectiveDate_month, jint effectiveDate_day
	, jint terminationDate_year, jint terminationDate_month, jint terminationDate_day, jdouble fixedPercentage)
{

	jdouble* marketQuotes_body = env->GetDoubleArrayElements(marketQuotes, NULL);
	jint* issueDates_year_body = env->GetIntArrayElements(issueDates_year, NULL);
	jint* issueDates_month_body = env->GetIntArrayElements(issueDates_month, NULL);
	jint* issueDates_day_body = env->GetIntArrayElements(issueDates_day, NULL);

	jint* maturities_year_body = env->GetIntArrayElements(maturities_year, NULL);
	jint* maturities_month_body = env->GetIntArrayElements(maturities_month, NULL);
	jint* maturities_day_body = env->GetIntArrayElements(maturities_day, NULL);

	jdouble* couponRates_body = env->GetDoubleArrayElements(couponRates, NULL);


	Calendar calendar = TARGET();

	Date settlementDate((int)settlementDate_day, (Month)settlementDate_month, settlementDate_year);

	settlementDate = calendar.adjust(settlementDate);

	Date todaysDate = calendar.advance(settlementDate, -fixingDays, Days);
	Settings::instance().evaluationDate() = todaysDate;

	ext::shared_ptr<Quote> zc3mRate(new SimpleQuote(zc3mQuote));
	ext::shared_ptr<Quote> zc6mRate(new SimpleQuote(zc6mQuote));
	ext::shared_ptr<Quote> zc1yRate(new SimpleQuote(zc1yQuote));

	DayCounter zcBondsDayCounter = Actual365Fixed();

	ext::shared_ptr<RateHelper> zc3m(new DepositRateHelper(
		Handle<Quote>(zc3mRate),
		3 * Months, fixingDays,
		calendar, ModifiedFollowing,
		true, zcBondsDayCounter));
	ext::shared_ptr<RateHelper> zc6m(new DepositRateHelper(
		Handle<Quote>(zc6mRate),
		6 * Months, fixingDays,
		calendar, ModifiedFollowing,
		true, zcBondsDayCounter));
	ext::shared_ptr<RateHelper> zc1y(new DepositRateHelper(
		Handle<Quote>(zc1yRate),
		1 * Years, fixingDays,
		calendar, ModifiedFollowing,
		true, zcBondsDayCounter));

	std::vector< ext::shared_ptr<SimpleQuote> > quote;
	for (Size i = 0; i < numberOfBonds; i++) {
		ext::shared_ptr<SimpleQuote> cp(new SimpleQuote(marketQuotes_body[i]));
		quote.push_back(cp);
	}


	RelinkableHandle<Quote>* quoteHandle = new RelinkableHandle<Quote>[numberOfBonds];
	for (Size i = 0; i < numberOfBonds; i++) {
		quoteHandle[i].linkTo(quote[i]);
	}

	std::vector<ext::shared_ptr<BondHelper> > bondsHelpers;

	for (Size i = 0; i < numberOfBonds; i++) {
		Date issueDate((int)(issueDates_day_body[i]), (Month)(issueDates_month_body[i]), issueDates_year_body[i]);
		Date maturity((int)(maturities_day_body[i]), (Month)(maturities_month_body[i]), maturities_year_body[i]);
		Schedule schedule(issueDate, maturity, Period(Semiannual), UnitedStates(UnitedStates::GovernmentBond),
			Unadjusted, Unadjusted, DateGeneration::Backward, false);

		ext::shared_ptr<FixedRateBondHelper> bondHelper(new FixedRateBondHelper(
			quoteHandle[i],
			settlementDays,
			100.0,
			schedule,
			std::vector<Rate>(1, couponRates_body[i]),
			ActualActual(ActualActual::Bond),
			Unadjusted,
			redemption,
			issueDate));

		bondsHelpers.push_back(bondHelper);
	}

	DayCounter termStructureDayCounter =
		ActualActual(ActualActual::ISDA);

	std::vector<ext::shared_ptr<RateHelper> > bondInstruments;

	bondInstruments.push_back(zc3m);
	bondInstruments.push_back(zc6m);
	bondInstruments.push_back(zc1y);

	for (Size i = 0; i < numberOfBonds; i++) {
		bondInstruments.push_back(bondsHelpers[i]);
	}

	ext::shared_ptr<YieldTermStructure> bondDiscountingTermStructure(
		new PiecewiseYieldCurve<Discount, LogLinear>(
			settlementDate, bondInstruments,
			termStructureDayCounter));

	RelinkableHandle<YieldTermStructure> discountingTermStructure;
	discountingTermStructure.linkTo(bondDiscountingTermStructure);

	Real faceAmount = 100;

	ext::shared_ptr<PricingEngine> bondEngine(
		new DiscountingBondEngine(discountingTermStructure));

	// Fixed 4.5% US Treasury Note
	Date effectiveDate((int)effectiveDate_day, (Month)effectiveDate_month, effectiveDate_year);
	Schedule fixedBondSchedule(effectiveDate,
		Date((int)terminationDate_day, (Month)terminationDate_month, terminationDate_year), Period(Semiannual),
		UnitedStates(UnitedStates::GovernmentBond),
		Unadjusted, Unadjusted, DateGeneration::Backward, false);

	FixedRateBond fixedRateBond(
		settlementDays,
		faceAmount,
		fixedBondSchedule,
		std::vector<Rate>(1, fixedPercentage),
		ActualActual(ActualActual::Bond),
		ModifiedFollowing,
		100.0, effectiveDate);

	fixedRateBond.setPricingEngine(bondEngine);

	/// Release

	env->ReleaseDoubleArrayElements(marketQuotes, marketQuotes_body, 0);

	env->ReleaseIntArrayElements(issueDates_year, issueDates_year_body, 0);
	env->ReleaseIntArrayElements(issueDates_month, issueDates_month_body, 0);
	env->ReleaseIntArrayElements(issueDates_day, issueDates_day_body, 0);

	env->ReleaseIntArrayElements(maturities_year, maturities_year_body, 0);
	env->ReleaseIntArrayElements(maturities_month, maturities_month_body, 0);
	env->ReleaseIntArrayElements(maturities_day, maturities_day_body, 0);

	env->ReleaseDoubleArrayElements(couponRates, couponRates_body, 0);

	const int returnSize = 7;
	jdoubleArray result = NULL;
	result = env->NewDoubleArray(returnSize);
	if (result == NULL) {
		return NULL; /* out of memory error thrown */
	}
	jdouble fill[returnSize] = { fixedRateBond.NPV() , fixedRateBond.cleanPrice() , fixedRateBond.dirtyPrice()
	, fixedRateBond.accruedAmount() , fixedRateBond.previousCouponRate(), fixedRateBond.nextCouponRate(),
	fixedRateBond.yield(Actual360(), Compounded, Annual) };

	// move from the temp structure to the java structure
	env->SetDoubleArrayRegion(result, 0, returnSize, fill);

	return result;
}

JNIEXPORT jdoubleArray JNICALL Java_com_na_1ho_pricer_jni_QuantJNI_priceEuropeanOptionMCSobol(JNIEnv* env, jobject thisObject
	, jint optionType
	, jint settlementDate_year, jint settlementDate_month, jint settlementDate_day
	, jint evaluationDate_year, jint evaluationDate_month, jint evaluationDate_day
	, jint maturityDate_year, jint maturityDate_month, jint maturityDate_day
	, jdouble riskFreeRate, jdouble dividendYield, jdouble volatility, jdouble strike, jdouble underlying, jint timeSteps, jint nSamples)
{
	
	Calendar calendar = TARGET();

	Date evaluationDate((int)evaluationDate_day, (Month)evaluationDate_month, evaluationDate_year);
	Settings::instance().evaluationDate() = evaluationDate;

	Date maturity((int)maturityDate_day, (Month)maturityDate_month, maturityDate_year);
	ext::shared_ptr<Exercise> europeanExercise(
		new EuropeanExercise(maturity));

	Date settlementDate((int)settlementDate_day, (Month)settlementDate_month, settlementDate_year);

	DayCounter dayCounter = Actual365Fixed();
	Option::Type type((Option::Type)optionType);

	Handle<Quote> underlyingH(
		ext::shared_ptr<Quote>(new SimpleQuote(underlying)));
	Handle<YieldTermStructure> flatTermStructure(
		ext::shared_ptr<YieldTermStructure>(
			new FlatForward(settlementDate, riskFreeRate, dayCounter)));
	Handle<YieldTermStructure> flatDividendTS(
		ext::shared_ptr<YieldTermStructure>(
			new FlatForward(settlementDate, dividendYield, dayCounter)));
	Handle<BlackVolTermStructure> flatVolTS(
		ext::shared_ptr<BlackVolTermStructure>(
			new BlackConstantVol(settlementDate, calendar, volatility,
				dayCounter)));

	ext::shared_ptr<StrikedTypePayoff> payoff(
		new PlainVanillaPayoff(type, strike));
	ext::shared_ptr<BlackScholesMertonProcess> bsmProcess(
		new BlackScholesMertonProcess(underlyingH, flatDividendTS,
			flatTermStructure, flatVolTS));
	VanillaOption europeanOption(payoff, europeanExercise);

	ext::shared_ptr<PricingEngine> mcengine2;
	mcengine2 = MakeMCEuropeanEngine<LowDiscrepancy>(bsmProcess)
		.withSteps(timeSteps)
		.withSamples(nSamples);
	europeanOption.setPricingEngine(mcengine2);

	const int returnSize = 1;
	jdoubleArray result = NULL;
	result = env->NewDoubleArray(returnSize);
	if (result == NULL) {
		return NULL; 
	}
	jdouble fill[returnSize] = { europeanOption.NPV() };

	env->SetDoubleArrayRegion(result, 0, returnSize, fill);

	return result;

}
