// DO NOT EDIT.  Make changes to OsmRelation.java instead.
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
public abstract class _OsmRelation extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "OsmRelation";

  // Attribute Keys
  public static final ERXKey<String> FIPS = new ERXKey<String>("fips");
  public static final ERXKey<String> NAME = new ERXKey<String>("name");
  public static final ERXKey<String> NOTE = new ERXKey<String>("note");
  public static final ERXKey<Long> PK = new ERXKey<Long>("pk");
  public static final ERXKey<String> PLACE = new ERXKey<String>("place");
  public static final ERXKey<Long> UPS = new ERXKey<Long>("ups");
  public static final ERXKey<String> URL = new ERXKey<String>("url");

  // Relationship Keys

  // Attributes
  public static final String FIPS_KEY = FIPS.key();
  public static final String NAME_KEY = NAME.key();
  public static final String NOTE_KEY = NOTE.key();
  public static final String PK_KEY = PK.key();
  public static final String PLACE_KEY = PLACE.key();
  public static final String UPS_KEY = UPS.key();
  public static final String URL_KEY = URL.key();

  // Relationships

  private static final Logger log = LoggerFactory.getLogger(_OsmRelation.class);

  public OsmRelation localInstanceIn(EOEditingContext editingContext) {
    OsmRelation localInstance = (OsmRelation)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String fips() {
    return (String) storedValueForKey(_OsmRelation.FIPS_KEY);
  }

  public void setFips(String value) {
    log.debug( "updating fips from {} to {}", fips(), value);
    takeStoredValueForKey(value, _OsmRelation.FIPS_KEY);
  }

  public String name() {
    return (String) storedValueForKey(_OsmRelation.NAME_KEY);
  }

  public void setName(String value) {
    log.debug( "updating name from {} to {}", name(), value);
    takeStoredValueForKey(value, _OsmRelation.NAME_KEY);
  }

  public String note() {
    return (String) storedValueForKey(_OsmRelation.NOTE_KEY);
  }

  public void setNote(String value) {
    log.debug( "updating note from {} to {}", note(), value);
    takeStoredValueForKey(value, _OsmRelation.NOTE_KEY);
  }

  public Long pk() {
    return (Long) storedValueForKey(_OsmRelation.PK_KEY);
  }

  public void setPk(Long value) {
    log.debug( "updating pk from {} to {}", pk(), value);
    takeStoredValueForKey(value, _OsmRelation.PK_KEY);
  }

  public String place() {
    return (String) storedValueForKey(_OsmRelation.PLACE_KEY);
  }

  public void setPlace(String value) {
    log.debug( "updating place from {} to {}", place(), value);
    takeStoredValueForKey(value, _OsmRelation.PLACE_KEY);
  }

  public Long ups() {
    return (Long) storedValueForKey(_OsmRelation.UPS_KEY);
  }

  public void setUps(Long value) {
    log.debug( "updating ups from {} to {}", ups(), value);
    takeStoredValueForKey(value, _OsmRelation.UPS_KEY);
  }

  public String url() {
    return (String) storedValueForKey(_OsmRelation.URL_KEY);
  }

  public void setUrl(String value) {
    log.debug( "updating url from {} to {}", url(), value);
    takeStoredValueForKey(value, _OsmRelation.URL_KEY);
  }


  public static OsmRelation createOsmRelation(EOEditingContext editingContext, Long pk
) {
    OsmRelation eo = (OsmRelation) EOUtilities.createAndInsertInstance(editingContext, _OsmRelation.ENTITY_NAME);
    eo.setPk(pk);
    return eo;
  }

  public static ERXFetchSpecification<OsmRelation> fetchSpec() {
    return new ERXFetchSpecification<OsmRelation>(_OsmRelation.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<OsmRelation> fetchAllOsmRelations(EOEditingContext editingContext) {
    return _OsmRelation.fetchAllOsmRelations(editingContext, null);
  }

  public static NSArray<OsmRelation> fetchAllOsmRelations(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _OsmRelation.fetchOsmRelations(editingContext, null, sortOrderings);
  }

  public static NSArray<OsmRelation> fetchOsmRelations(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<OsmRelation> fetchSpec = new ERXFetchSpecification<OsmRelation>(_OsmRelation.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<OsmRelation> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static OsmRelation fetchOsmRelation(EOEditingContext editingContext, String keyName, Object value) {
    return _OsmRelation.fetchOsmRelation(editingContext, ERXQ.equals(keyName, value));
  }

  public static OsmRelation fetchOsmRelation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<OsmRelation> eoObjects = _OsmRelation.fetchOsmRelations(editingContext, qualifier, null);
    OsmRelation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one OsmRelation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OsmRelation fetchRequiredOsmRelation(EOEditingContext editingContext, String keyName, Object value) {
    return _OsmRelation.fetchRequiredOsmRelation(editingContext, ERXQ.equals(keyName, value));
  }

  public static OsmRelation fetchRequiredOsmRelation(EOEditingContext editingContext, EOQualifier qualifier) {
    OsmRelation eoObject = _OsmRelation.fetchOsmRelation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no OsmRelation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OsmRelation localInstanceIn(EOEditingContext editingContext, OsmRelation eo) {
    OsmRelation localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
