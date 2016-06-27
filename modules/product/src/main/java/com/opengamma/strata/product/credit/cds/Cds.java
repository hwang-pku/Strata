package com.opengamma.strata.product.credit.cds;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.Resolvable;
import com.opengamma.strata.basics.StandardId;
import com.opengamma.strata.basics.date.DaysAdjustment;

@BeanDefinition
public final class Cds
    implements Resolvable<ResolvedCds>, ImmutableBean, Serializable {

  @PropertyDefinition(validate = "notNull")
  private final CdsPremiumLeg premiumLeg;

  //  @PropertyDefinition(validate = "ArgChecker.notNegative")  // TODO should be in market data
  //  private final double recoveryRate;

  @PropertyDefinition(validate = "notNull")
  private final boolean protectStart; // TODO only applied to protection leg and accrued on default
  /**
   * The number of days between valuation date and settlement date.
   * <p>
   * This is used to compute clean price.
   * The clean price is the relative price to be paid at the standard settlement date in exchange for the bond.
   * <p>
   * It is usually one business day for US treasuries and UK Gilts and three days for Euroland government bonds.
   */
  @PropertyDefinition(validate = "notNull")
  private final DaysAdjustment settlementDateOffset;
  /**
   * The legal entity identifier.
   * <p>
   * This identifier is used for the legal entity that issues the bond.
   */
  @PropertyDefinition(validate = "notNull")
  private final StandardId legalEntityId;

  //-------------------------------------------------------------------------
  @Override
  public ResolvedCds resolve(ReferenceData refData) {
    ResolvedCdsPremiumLeg resolvedPremiumLeg = premiumLeg.resolve(refData);

    return ResolvedCds.builder()
        .legalEntityId(legalEntityId)
        .protectStart(protectStart)
        .resolvedPremiumLeg(resolvedPremiumLeg)
        .settlementDateOffset(settlementDateOffset)
        .build();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code Cds}.
   * @return the meta-bean, not null
   */
  public static Cds.Meta meta() {
    return Cds.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(Cds.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static Cds.Builder builder() {
    return new Cds.Builder();
  }

  private Cds(
      CdsPremiumLeg premiumLeg,
      boolean protectStart,
      DaysAdjustment settlementDateOffset,
      StandardId legalEntityId) {
    JodaBeanUtils.notNull(premiumLeg, "premiumLeg");
    JodaBeanUtils.notNull(protectStart, "protectStart");
    JodaBeanUtils.notNull(settlementDateOffset, "settlementDateOffset");
    JodaBeanUtils.notNull(legalEntityId, "legalEntityId");
    this.premiumLeg = premiumLeg;
    this.protectStart = protectStart;
    this.settlementDateOffset = settlementDateOffset;
    this.legalEntityId = legalEntityId;
  }

  @Override
  public Cds.Meta metaBean() {
    return Cds.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the premiumLeg.
   * @return the value of the property, not null
   */
  public CdsPremiumLeg getPremiumLeg() {
    return premiumLeg;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the protectStart.
   * @return the value of the property, not null
   */
  public boolean isProtectStart() {
    return protectStart;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of days between valuation date and settlement date.
   * <p>
   * This is used to compute clean price.
   * The clean price is the relative price to be paid at the standard settlement date in exchange for the bond.
   * <p>
   * It is usually one business day for US treasuries and UK Gilts and three days for Euroland government bonds.
   * @return the value of the property, not null
   */
  public DaysAdjustment getSettlementDateOffset() {
    return settlementDateOffset;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the legal entity identifier.
   * <p>
   * This identifier is used for the legal entity that issues the bond.
   * @return the value of the property, not null
   */
  public StandardId getLegalEntityId() {
    return legalEntityId;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      Cds other = (Cds) obj;
      return JodaBeanUtils.equal(premiumLeg, other.premiumLeg) &&
          (protectStart == other.protectStart) &&
          JodaBeanUtils.equal(settlementDateOffset, other.settlementDateOffset) &&
          JodaBeanUtils.equal(legalEntityId, other.legalEntityId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(premiumLeg);
    hash = hash * 31 + JodaBeanUtils.hashCode(protectStart);
    hash = hash * 31 + JodaBeanUtils.hashCode(settlementDateOffset);
    hash = hash * 31 + JodaBeanUtils.hashCode(legalEntityId);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("Cds{");
    buf.append("premiumLeg").append('=').append(premiumLeg).append(',').append(' ');
    buf.append("protectStart").append('=').append(protectStart).append(',').append(' ');
    buf.append("settlementDateOffset").append('=').append(settlementDateOffset).append(',').append(' ');
    buf.append("legalEntityId").append('=').append(JodaBeanUtils.toString(legalEntityId));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code Cds}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code premiumLeg} property.
     */
    private final MetaProperty<CdsPremiumLeg> premiumLeg = DirectMetaProperty.ofImmutable(
        this, "premiumLeg", Cds.class, CdsPremiumLeg.class);
    /**
     * The meta-property for the {@code protectStart} property.
     */
    private final MetaProperty<Boolean> protectStart = DirectMetaProperty.ofImmutable(
        this, "protectStart", Cds.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code settlementDateOffset} property.
     */
    private final MetaProperty<DaysAdjustment> settlementDateOffset = DirectMetaProperty.ofImmutable(
        this, "settlementDateOffset", Cds.class, DaysAdjustment.class);
    /**
     * The meta-property for the {@code legalEntityId} property.
     */
    private final MetaProperty<StandardId> legalEntityId = DirectMetaProperty.ofImmutable(
        this, "legalEntityId", Cds.class, StandardId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "premiumLeg",
        "protectStart",
        "settlementDateOffset",
        "legalEntityId");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575219767:  // premiumLeg
          return premiumLeg;
        case 33810131:  // protectStart
          return protectStart;
        case 135924714:  // settlementDateOffset
          return settlementDateOffset;
        case 866287159:  // legalEntityId
          return legalEntityId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public Cds.Builder builder() {
      return new Cds.Builder();
    }

    @Override
    public Class<? extends Cds> beanType() {
      return Cds.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code premiumLeg} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CdsPremiumLeg> premiumLeg() {
      return premiumLeg;
    }

    /**
     * The meta-property for the {@code protectStart} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Boolean> protectStart() {
      return protectStart;
    }

    /**
     * The meta-property for the {@code settlementDateOffset} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DaysAdjustment> settlementDateOffset() {
      return settlementDateOffset;
    }

    /**
     * The meta-property for the {@code legalEntityId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<StandardId> legalEntityId() {
      return legalEntityId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575219767:  // premiumLeg
          return ((Cds) bean).getPremiumLeg();
        case 33810131:  // protectStart
          return ((Cds) bean).isProtectStart();
        case 135924714:  // settlementDateOffset
          return ((Cds) bean).getSettlementDateOffset();
        case 866287159:  // legalEntityId
          return ((Cds) bean).getLegalEntityId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code Cds}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<Cds> {

    private CdsPremiumLeg premiumLeg;
    private boolean protectStart;
    private DaysAdjustment settlementDateOffset;
    private StandardId legalEntityId;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(Cds beanToCopy) {
      this.premiumLeg = beanToCopy.getPremiumLeg();
      this.protectStart = beanToCopy.isProtectStart();
      this.settlementDateOffset = beanToCopy.getSettlementDateOffset();
      this.legalEntityId = beanToCopy.getLegalEntityId();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575219767:  // premiumLeg
          return premiumLeg;
        case 33810131:  // protectStart
          return protectStart;
        case 135924714:  // settlementDateOffset
          return settlementDateOffset;
        case 866287159:  // legalEntityId
          return legalEntityId;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 575219767:  // premiumLeg
          this.premiumLeg = (CdsPremiumLeg) newValue;
          break;
        case 33810131:  // protectStart
          this.protectStart = (Boolean) newValue;
          break;
        case 135924714:  // settlementDateOffset
          this.settlementDateOffset = (DaysAdjustment) newValue;
          break;
        case 866287159:  // legalEntityId
          this.legalEntityId = (StandardId) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public Cds build() {
      return new Cds(
          premiumLeg,
          protectStart,
          settlementDateOffset,
          legalEntityId);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the premiumLeg.
     * @param premiumLeg  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder premiumLeg(CdsPremiumLeg premiumLeg) {
      JodaBeanUtils.notNull(premiumLeg, "premiumLeg");
      this.premiumLeg = premiumLeg;
      return this;
    }

    /**
     * Sets the protectStart.
     * @param protectStart  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder protectStart(boolean protectStart) {
      JodaBeanUtils.notNull(protectStart, "protectStart");
      this.protectStart = protectStart;
      return this;
    }

    /**
     * Sets the number of days between valuation date and settlement date.
     * <p>
     * This is used to compute clean price.
     * The clean price is the relative price to be paid at the standard settlement date in exchange for the bond.
     * <p>
     * It is usually one business day for US treasuries and UK Gilts and three days for Euroland government bonds.
     * @param settlementDateOffset  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder settlementDateOffset(DaysAdjustment settlementDateOffset) {
      JodaBeanUtils.notNull(settlementDateOffset, "settlementDateOffset");
      this.settlementDateOffset = settlementDateOffset;
      return this;
    }

    /**
     * Sets the legal entity identifier.
     * <p>
     * This identifier is used for the legal entity that issues the bond.
     * @param legalEntityId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder legalEntityId(StandardId legalEntityId) {
      JodaBeanUtils.notNull(legalEntityId, "legalEntityId");
      this.legalEntityId = legalEntityId;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("Cds.Builder{");
      buf.append("premiumLeg").append('=').append(JodaBeanUtils.toString(premiumLeg)).append(',').append(' ');
      buf.append("protectStart").append('=').append(JodaBeanUtils.toString(protectStart)).append(',').append(' ');
      buf.append("settlementDateOffset").append('=').append(JodaBeanUtils.toString(settlementDateOffset)).append(',').append(' ');
      buf.append("legalEntityId").append('=').append(JodaBeanUtils.toString(legalEntityId));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}