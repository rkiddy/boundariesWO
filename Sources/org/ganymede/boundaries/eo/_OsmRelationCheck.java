// DO NOT EDIT.  Make changes to OsmRelationCheck.java instead.
package org.ganymede.boundaries.eo;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

import er.extensions.eof.*;
import er.extensions.foundation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class _OsmRelationCheck extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "OsmRelationCheck";

  // Attribute Keys
  public static final ERXKey<Long> CHECK_RESULT = new ERXKey<Long>("checkResult");
  public static final ERXKey<NSTimestamp> END_CONDITION = new ERXKey<NSTimestamp>("endCondition");
  public static final ERXKey<NSTimestamp> START_CONDITION = new ERXKey<NSTimestamp>("startCondition");

  // Relationship Keys
  public static final ERXKey<org.ganymede.boundaries.eo.OsmRelation> RELATION = new ERXKey<org.ganymede.boundaries.eo.OsmRelation>("relation");

  // Attributes
  public static final String CHECK_RESULT_KEY = CHECK_RESULT.key();
  public static final String END_CONDITION_KEY = END_CONDITION.key();
  public static final String START_CONDITION_KEY = START_CONDITION.key();

  // Relationships
  public static final String RELATION_KEY = RELATION.key();

  private static final Logger log = LoggerFactory.getLogger(_OsmRelationCheck.class);

  public OsmRelationCheck localInstanceIn(EOEditingContext editingContext) {
    OsmRelationCheck localInstance = (OsmRelationCheck)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public Long checkResult() {
    return (Long) storedValueForKey(_OsmRelationCheck.CHECK_RESULT_KEY);
  }

  public void setCheckResult(Long value) {
    log.debug( "updating checkResult from {} to {}", checkResult(), value);
    takeStoredValueForKey(value, _OsmRelationCheck.CHECK_RESULT_KEY);
  }

  public NSTimestamp endCondition() {
    return (NSTimestamp) storedValueForKey(_OsmRelationCheck.END_CONDITION_KEY);
  }

  public void setEndCondition(NSTimestamp value) {
    log.debug( "updating endCondition from {} to {}", endCondition(), value);
    takeStoredValueForKey(value, _OsmRelationCheck.END_CONDITION_KEY);
  }

  public NSTimestamp startCondition() {
    return (NSTimestamp) storedValueForKey(_OsmRelationCheck.START_CONDITION_KEY);
  }

  public void setStartCondition(NSTimestamp value) {
    log.debug( "updating startCondition from {} to {}", startCondition(), value);
    takeStoredValueForKey(value, _OsmRelationCheck.START_CONDITION_KEY);
  }

  public org.ganymede.boundaries.eo.OsmRelation relation() {
    return (org.ganymede.boundaries.eo.OsmRelation)storedValueForKey(_OsmRelationCheck.RELATION_KEY);
  }

  public void setRelation(org.ganymede.boundaries.eo.OsmRelation value) {
    takeStoredValueForKey(value, _OsmRelationCheck.RELATION_KEY);
  }

  public void setRelationRelationship(org.ganymede.boundaries.eo.OsmRelation value) {
    log.debug("updating relation from {} to {}", relation(), value);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      setRelation(value);
    }
    else if (value == null) {
      org.ganymede.boundaries.eo.OsmRelation oldValue = relation();
      if (oldValue != null) {
        removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _OsmRelationCheck.RELATION_KEY);
      }
    } else {
      addObjectToBothSidesOfRelationshipWithKey(value, _OsmRelationCheck.RELATION_KEY);
    }
  }


  public static OsmRelationCheck createOsmRelationCheck(EOEditingContext editingContext, Long checkResult
, NSTimestamp endCondition
, NSTimestamp startCondition
, org.ganymede.boundaries.eo.OsmRelation relation) {
    OsmRelationCheck eo = (OsmRelationCheck) EOUtilities.createAndInsertInstance(editingContext, _OsmRelationCheck.ENTITY_NAME);
    eo.setCheckResult(checkResult);
    eo.setEndCondition(endCondition);
    eo.setStartCondition(startCondition);
    eo.setRelationRelationship(relation);
    return eo;
  }

  public static ERXFetchSpecification<OsmRelationCheck> fetchSpec() {
    return new ERXFetchSpecification<OsmRelationCheck>(_OsmRelationCheck.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<OsmRelationCheck> fetchAllOsmRelationChecks(EOEditingContext editingContext) {
    return _OsmRelationCheck.fetchAllOsmRelationChecks(editingContext, null);
  }

  public static NSArray<OsmRelationCheck> fetchAllOsmRelationChecks(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _OsmRelationCheck.fetchOsmRelationChecks(editingContext, null, sortOrderings);
  }

  public static NSArray<OsmRelationCheck> fetchOsmRelationChecks(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<OsmRelationCheck> fetchSpec = new ERXFetchSpecification<OsmRelationCheck>(_OsmRelationCheck.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<OsmRelationCheck> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static OsmRelationCheck fetchOsmRelationCheck(EOEditingContext editingContext, String keyName, Object value) {
    return _OsmRelationCheck.fetchOsmRelationCheck(editingContext, ERXQ.equals(keyName, value));
  }

  public static OsmRelationCheck fetchOsmRelationCheck(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<OsmRelationCheck> eoObjects = _OsmRelationCheck.fetchOsmRelationChecks(editingContext, qualifier, null);
    OsmRelationCheck eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one OsmRelationCheck that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OsmRelationCheck fetchRequiredOsmRelationCheck(EOEditingContext editingContext, String keyName, Object value) {
    return _OsmRelationCheck.fetchRequiredOsmRelationCheck(editingContext, ERXQ.equals(keyName, value));
  }

  public static OsmRelationCheck fetchRequiredOsmRelationCheck(EOEditingContext editingContext, EOQualifier qualifier) {
    OsmRelationCheck eoObject = _OsmRelationCheck.fetchOsmRelationCheck(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no OsmRelationCheck that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OsmRelationCheck localInstanceIn(EOEditingContext editingContext, OsmRelationCheck eo) {
    OsmRelationCheck localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
