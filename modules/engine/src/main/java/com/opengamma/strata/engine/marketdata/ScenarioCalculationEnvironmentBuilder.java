/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.engine.marketdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.opengamma.strata.basics.market.MarketDataId;
import com.opengamma.strata.basics.market.ObservableId;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.Messages;
import com.opengamma.strata.collect.result.Failure;
import com.opengamma.strata.collect.result.Result;
import com.opengamma.strata.collect.timeseries.LocalDateDoubleTimeSeries;

// TODO This needs copies of all of the add methods that put stuff into the base data
/**
 * A mutable builder for building an instance of {@link ScenarioCalculationEnvironment}.
 */
public final class ScenarioCalculationEnvironmentBuilder {

  /** Builder for the base market data. */
  private final CalculationEnvironmentBuilder baseBuilder;

  /** The number of scenarios. */
  private final int scenarioCount;

  /** The valuation dates of the scenarios, one for each scenario. */
  private final List<LocalDate> valuationDates = new ArrayList<>();

  /**
   * The market data values for the scenarios, keyed by the ID of the market data.
   * The number of values for each key is the same as the number of scenarios.
   */
  private final ListMultimap<MarketDataId<?>, Object> values = ArrayListMultimap.create();

  /**
   * The time series of market data for the scenarios, keyed by the ID of the market data.
   * The number of values for each key is the same as the number of scenarios.
   */
  private final Map<ObservableId, LocalDateDoubleTimeSeries> timeSeries = new HashMap<>();

  /** The global market data values that are applicable to all scenarios. */
  private final Map<MarketDataId<?>, Object> globalValues = new HashMap<>();

  /** Details of failures when building single market data values. */
  private final Map<MarketDataId<?>, Failure> singleValueFailures = new HashMap<>();

  /** Details of failures when building time series of market data values. */
  private final Map<MarketDataId<?>, Failure> timeSeriesFailures = new HashMap<>();

  /**
   * Returns a new builder where every scenario has the same valuation date.
   *
   * @param scenarioCount the number of scenarios
   * @param valuationDate the valuation date for all scenarios
   */
  ScenarioCalculationEnvironmentBuilder(int scenarioCount, LocalDate valuationDate) {
    this.scenarioCount = ArgChecker.notNegativeOrZero(scenarioCount, "scenarioCount");
    this.baseBuilder = CalculationEnvironment.builder(valuationDate);
    valuationDate(valuationDate);
  }

  /**
   * Returns a new builder where every scenario has the same valuation date.
   * <p>
   * This is package-private because it is intended to be used by {@code ScenarioMarketData.builder()}.
   *
   * @param scenarioCount  the number of scenarios
   * @param valuationDates  the valuation dates for the scenarios
   * @param values  the single market data values
   * @param timeSeries  the time series of market data values
   * @param globalValues  the single market data values applicable to all scenarios
   * @param singleValueFailures  the single value failures
   * @param timeSeriesFailures  the time-series failures
   */
  ScenarioCalculationEnvironmentBuilder(
      CalculationEnvironment baseData,
      int scenarioCount,
      List<LocalDate> valuationDates,
      ListMultimap<MarketDataId<?>, ?> values,
      Map<ObservableId, LocalDateDoubleTimeSeries> timeSeries,
      Map<? extends MarketDataId<?>, Object> globalValues,
      Map<MarketDataId<?>, Failure> singleValueFailures,
      Map<MarketDataId<?>, Failure> timeSeriesFailures) {

    ArgChecker.notNegativeOrZero(scenarioCount, "scenarioCount");
    ArgChecker.notNull(valuationDates, "valuationDates");
    ArgChecker.notNull(values, "values");
    ArgChecker.notNull(timeSeries, "timeSeries");
    ArgChecker.notNull(globalValues, "globalValues");

    this.baseBuilder = baseData.toBuilder();
    this.scenarioCount = scenarioCount;
    this.values.putAll(values);
    this.timeSeries.putAll(timeSeries);
    this.globalValues.putAll(globalValues);
    this.singleValueFailures.putAll(singleValueFailures);
    this.timeSeriesFailures.putAll(timeSeriesFailures);
    valuationDates(valuationDates);
  }

  /**
   * Sets the valuation date for all scenarios.
   *
   * @param valuationDate the valuation date for all scenarios
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder valuationDate(LocalDate valuationDate) {
    ArgChecker.notNull(valuationDate, "valuationDate");
    valuationDates.addAll(Collections.nCopies(scenarioCount, valuationDate));
    return this;
  }

  /**
   * Sets the valuation date for all scenarios. The number of date arguments must be the same as
   * the number of scenarios.
   *
   * @param valuationDates the valuation dates for the scenarios, one for each scenario
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder valuationDates(LocalDate... valuationDates) {
    ArgChecker.notNull(valuationDates, "valuationDates");
    checkLength(valuationDates.length, "valuation dates");
    this.valuationDates.clear();
    this.valuationDates.addAll(Arrays.asList(valuationDates));
    return this;
  }

  /**
   * Sets the valuation date for all scenarios. The number of date arguments must be the same as
   * the number of scenarios.
   *
   * @param valuationDates the valuation dates for the scenarios, one for each scenario
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder valuationDates(List<LocalDate> valuationDates) {
    ArgChecker.notNull(valuationDates, "valuationDates");
    checkLength(valuationDates.size(), "valuation dates");
    this.valuationDates.clear();
    this.valuationDates.addAll(valuationDates);
    return this;
  }

  /**
   * Adds market data values for all scenarios. The number of values must be the same as the number
   * of scenarios.
   *
   * @param id the ID of the market data values
   * @param values the market data values, one for each scenario
   * @param <T> the type of the market data values
   * @return this builder
   */
  @SafeVarargs
  public final <T> ScenarioCalculationEnvironmentBuilder addValues(MarketDataId<T> id, T... values) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(values, "values");
    checkLength(values.length, "values");
    this.values.putAll(id, Arrays.asList(values));
    singleValueFailures.remove(id);
    return this;
  }

  /**
   * Adds market data values for all scenarios.
   * The number of values must be the same as the number of scenarios.
   *
   * @param id the ID of the market data values
   * @param values the market data values, one for each scenario
   * @param <T> the type of the market data values
   * @return this builder
   */
  public <T> ScenarioCalculationEnvironmentBuilder addValues(MarketDataId<T> id, List<T> values) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(values, "values");
    checkLength(values.size(), "values");
    this.values.putAll(id, values);
    singleValueFailures.remove(id);
    return this;
  }

  /**
   * Adds market data values for all scenarios.
   * The number of values must be the same as the number of scenarios.
   * <p>
   * The type of the values is checked to ensure it is compatible with the ID.
   *
   * @param id the ID of the market data values
   * @param values the market data values, one for each scenario
   * @param <T> the type of the market data values
   * @return this builder
   */
  <T, V> ScenarioCalculationEnvironmentBuilder addValuesUnsafe(MarketDataId<T> id, List<V> values) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(values, "values");
    checkLength(values.size(), "values");
    for (V value : values) {
      this.values.put(id, id.getMarketDataType().cast(value));
      singleValueFailures.remove(id);
    }
    return this;
  }

  /**
   * Adds a result for a single item of market data, replacing any existing value with the same ID.
   *
   * @param id  the ID of the market data
   * @param result  a result containing the market data value or details of why it could not be provided
   * @param <T>  the type of the market data value
   * @return this builder
   */
  public <T> ScenarioCalculationEnvironmentBuilder addResult(MarketDataId<T> id, Result<List<T>> result) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(result, "result");

    if (result.isSuccess()) {
      values.putAll(id, result.getValue());
      singleValueFailures.remove(id);
    } else {
      singleValueFailures.put(id, result.getFailure());
      values.removeAll(id);
    }
    return this;
  }

  /**
   * Adds a result for a single item of market data, replacing any existing value with the same ID.
   *
   * @param id  the ID of the market data
   * @param result  a result containing the market data value or details of why it could not be provided
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder addResultUnsafe(MarketDataId<?> id, Result<List<?>> result) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(result, "result");

    if (result.isSuccess()) {
      values.putAll(id, result.getValue());
      singleValueFailures.remove(id);
    } else {
      singleValueFailures.put(id, result.getFailure());
      values.removeAll(id);
    }
    return this;
  }

  /**
   * Adds a time series of market data values.
   *
   * @param id  the ID of the market data values
   * @param timeSeries  the time series of market data values
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder addTimeSeries(ObservableId id, LocalDateDoubleTimeSeries timeSeries) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(timeSeries, "timeSeries");
    this.timeSeries.put(id, timeSeries);
    timeSeriesFailures.remove(id);
    return this;
  }

  /**
   * Adds multiple time series of market data values.
   *
   * @param timeSeries  a map of time series of market data values, keyed by the ID of the market data
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder addTimeSeries(
      Map<? extends ObservableId, LocalDateDoubleTimeSeries> timeSeries) {

    ArgChecker.notNull(timeSeries, "timeSeries");
    this.timeSeries.putAll(timeSeries);
    timeSeries.keySet().stream().forEach(timeSeriesFailures::remove);
    return this;
  }

  /**
   * Adds a time series of observable market data values, replacing any existing time series with the same ID.
   *
   * @param id  the ID of the values
   * @param result  a time series of observable market data values
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder addTimeSeriesResult(
      ObservableId id,
      Result<LocalDateDoubleTimeSeries> result) {

    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(result, "result");

    if (result.isSuccess()) {
      timeSeries.put(id, result.getValue());
      timeSeriesFailures.remove(id);
    } else {
      timeSeriesFailures.put(id, result.getFailure());
      timeSeries.remove(id);
    }
    return this;
  }

  /**
   * Adds a value to the base market data which is shared between all scenarios.
   *
   * @param id  the ID of the market data value
   * @param value  the market data value
   * @param <T>  the type of the market data value
   * @return this builder
   */
  public <T> ScenarioCalculationEnvironmentBuilder addBaseValue(MarketDataId<T> id, T value) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(value, "value");
    baseBuilder.addValue(id, value);
    return this;
  }

  /**
   * Adds a value to the base market data which is shared between all scenarios.
   *
   * @param id  the ID of the market data value
   * @param value  the market data value
   * @param <T>  the type of the market data value
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder addBaseValueUnsafe(MarketDataId<?> id, Object value) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(value, "value");
    baseBuilder.addValueUnsafe(id, value);
    return this;
  }

  /**
   * Adds a result for a single item of market data, replacing any existing value with the same ID.
   *
   * @param id  the ID of the market data
   * @param result  a result containing the market data value or details of why it could not be provided
   * @param <T>  the type of the market data value
   * @return this builder
   */
  public <T> ScenarioCalculationEnvironmentBuilder addBaseResult(MarketDataId<T> id, Result<T> result) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(result, "result");

    if (result.isSuccess()) {
      values.put(id, result.getValue());
      singleValueFailures.remove(id);
    } else {
      singleValueFailures.put(id, result.getFailure());
      values.removeAll(id);
    }
    return this;
  }

  /**
   * Adds a result for a single item of market data, replacing any existing value with the same ID.
   *
   * @param id  the ID of the market data
   * @param result  a result containing the market data value or details of why it could not be provided
   * @param <T>  the type of the market data value
   * @return this builder
   */
  public ScenarioCalculationEnvironmentBuilder addBaseResultUnsafe(MarketDataId<?> id, Result<?> result) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(result, "result");

    if (result.isSuccess()) {
      values.put(id, result.getValue());
      singleValueFailures.remove(id);
    } else {
      singleValueFailures.put(id, result.getFailure());
      values.removeAll(id);
    }
    return this;
  }

    /**
     * Adds a global value that is applicable to all scenarios.
     *
     * @param id the identifier to associate the value with
     * @param value the value to add
     * @return this builder
     */
  public <T> ScenarioCalculationEnvironmentBuilder addGlobalValue(MarketDataId<T> id, T value) {
    ArgChecker.notNull(id, "id");
    ArgChecker.notNull(value, "value");
    globalValues.put(id, value);
    return this;
  }

  /**
   * Returns a set of scenario market data built from the data in this builder.
   *
   * @return a set of scenario market data built from the data in this builder
   */
  public ScenarioCalculationEnvironment build() {
    return new ScenarioCalculationEnvironment(
        baseBuilder.build(),
        scenarioCount,
        valuationDates,
        values,
        timeSeries,
        globalValues,
        singleValueFailures,
        timeSeriesFailures);
  }

  private void checkLength(int length, String itemName) {
    if (length != scenarioCount) {
      throw new IllegalArgumentException(
          Messages.format(
              "The number of {} ({}) must be the same as the number of scenarios ({})",
              itemName,
              length,
              scenarioCount));
    }
  }
}
