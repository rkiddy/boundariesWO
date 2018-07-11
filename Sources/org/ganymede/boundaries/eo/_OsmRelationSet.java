// DO NOT EDIT.  Make changes to OsmRelationSet.java instead.
package org.ganymede.boundaries.eo;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

import er.extensions.eof.*;
import er.extensions.eof.ERXKey.Type;
import er.extensions.foundation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class _OsmRelationSet extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "OsmRelationSet";

  // Attribute Keys
  public static final ERXKey<String> NAME = new ERXKey<String>("name", Type.Attribute);

  // Relationship Keys
  public static final ERXKey<org.ganymede.boundaries.eo.OsmRelation> RELATIONS = new ERXKey<org.ganymede.boundaries.eo.OsmRelation>("relations", Type.ToManyRelationship);
  public static final ERXKey<er.extensions.eof.ERXGenericRecord> RELATIONS_SET_JOINS = new ERXKey<er.extensions.eof.ERXGenericRecord>("relationsSetJoins", Type.ToManyRelationship);

  // Attributes
  public static final String NAME_KEY = NAME.key();

  // Relationships
  public static final String RELATIONS_KEY = RELATIONS.key();
  public static final String RELATIONS_SET_JOINS_KEY = RELATIONS_SET_JOINS.key();

  private static final Logger log = LoggerFactory.getLogger(_OsmRelationSet.class);

  public OsmRelationSet localInstanceIn(EOEditingContext editingContext) {
    OsmRelationSet localInstance = (OsmRelationSet)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String name() {
    return (String) storedValueForKey(_OsmRelationSet.NAME_KEY);
  }

  public void setName(String value) {
    log.debug( "updating name from {} to {}", name(), value);
    takeStoredValueForKey(value, _OsmRelationSet.NAME_KEY);
  }

  public NSArray<org.ganymede.boundaries.eo.OsmRelation> relations() {
    return (NSArray<org.ganymede.boundaries.eo.OsmRelation>)storedValueForKey(_OsmRelationSet.RELATIONS_KEY);
  }

  public NSArray<org.ganymede.boundaries.eo.OsmRelation> relations(EOQualifier qualifier) {
    return relations(qualifier, null);
  }

  public NSArray<org.ganymede.boundaries.eo.OsmRelation> relations(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<org.ganymede.boundaries.eo.OsmRelation> results;
      results = relations();
      if (qualifier != null) {
        results = (NSArray<org.ganymede.boundaries.eo.OsmRelation>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.ganymede.boundaries.eo.OsmRelation>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }

  public void addToRelations(org.ganymede.boundaries.eo.OsmRelation object) {
    includeObjectIntoPropertyWithKey(object, _OsmRelationSet.RELATIONS_KEY);
  }

  public void removeFromRelations(org.ganymede.boundaries.eo.OsmRelation object) {
    excludeObjectFromPropertyWithKey(object, _OsmRelationSet.RELATIONS_KEY);
  }

  public void addToRelationsRelationship(org.ganymede.boundaries.eo.OsmRelation object) {
    log.debug("adding {} to relations relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      addToRelations(object);
    }
    else {
      addObjectToBothSidesOfRelationshipWithKey(object, _OsmRelationSet.RELATIONS_KEY);
    }
  }

  public void removeFromRelationsRelationship(org.ganymede.boundaries.eo.OsmRelation object) {
    log.debug("removing {} from relations relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      removeFromRelations(object);
    }
    else {
      removeObjectFromBothSidesOfRelationshipWithKey(object, _OsmRelationSet.RELATIONS_KEY);
    }
  }

  public org.ganymede.boundaries.eo.OsmRelation createRelationsRelationship() {
    EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(editingContext(),  org.ganymede.boundaries.eo.OsmRelation.ENTITY_NAME );
    addObjectToBothSidesOfRelationshipWithKey(eo, _OsmRelationSet.RELATIONS_KEY);
    return (org.ganymede.boundaries.eo.OsmRelation) eo;
  }

  public void deleteRelationsRelationship(org.ganymede.boundaries.eo.OsmRelation object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _OsmRelationSet.RELATIONS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllRelationsRelationships() {
    Enumeration<org.ganymede.boundaries.eo.OsmRelation> objects = relations().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteRelationsRelationship(objects.nextElement());
    }
  }

  public NSArray<er.extensions.eof.ERXGenericRecord> relationsSetJoins() {
    return (NSArray<er.extensions.eof.ERXGenericRecord>)storedValueForKey(_OsmRelationSet.RELATIONS_SET_JOINS_KEY);
  }

  public NSArray<er.extensions.eof.ERXGenericRecord> relationsSetJoins(EOQualifier qualifier) {
    return relationsSetJoins(qualifier, null, false);
  }

  public NSArray<er.extensions.eof.ERXGenericRecord> relationsSetJoins(EOQualifier qualifier, boolean fetch) {
    return relationsSetJoins(qualifier, null, fetch);
  }

  public NSArray<er.extensions.eof.ERXGenericRecord> relationsSetJoins(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.extensions.eof.ERXGenericRecord> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = ERXQ.equals("relationSet", this);

      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        fullQualifier = ERXQ.and(qualifier, inverseQualifier);
      }

      ERXFetchSpecification<OsmRelationSet> fetchSpec = new ERXFetchSpecification<OsmRelationSet>("OsmRelationSetJoin", qualifier, sortOrderings);
      results = (NSArray<er.extensions.eof.ERXGenericRecord>)editingContext().objectsWithFetchSpecification(fetchSpec);
    }
    else {
      results = relationsSetJoins();
      if (qualifier != null) {
        results = (NSArray<er.extensions.eof.ERXGenericRecord>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.extensions.eof.ERXGenericRecord>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }

  public void addToRelationsSetJoins(er.extensions.eof.ERXGenericRecord object) {
    includeObjectIntoPropertyWithKey(object, _OsmRelationSet.RELATIONS_SET_JOINS_KEY);
  }

  public void removeFromRelationsSetJoins(er.extensions.eof.ERXGenericRecord object) {
    excludeObjectFromPropertyWithKey(object, _OsmRelationSet.RELATIONS_SET_JOINS_KEY);
  }

  public void addToRelationsSetJoinsRelationship(er.extensions.eof.ERXGenericRecord object) {
    log.debug("adding {} to relationsSetJoins relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      addToRelationsSetJoins(object);
    }
    else {
      addObjectToBothSidesOfRelationshipWithKey(object, _OsmRelationSet.RELATIONS_SET_JOINS_KEY);
    }
  }

  public void removeFromRelationsSetJoinsRelationship(er.extensions.eof.ERXGenericRecord object) {
    log.debug("removing {} from relationsSetJoins relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      removeFromRelationsSetJoins(object);
    }
    else {
      removeObjectFromBothSidesOfRelationshipWithKey(object, _OsmRelationSet.RELATIONS_SET_JOINS_KEY);
    }
  }

  public er.extensions.eof.ERXGenericRecord createRelationsSetJoinsRelationship() {
    EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(editingContext(), "OsmRelationSetJoin");
    addObjectToBothSidesOfRelationshipWithKey(eo, _OsmRelationSet.RELATIONS_SET_JOINS_KEY);
    return (er.extensions.eof.ERXGenericRecord) eo;
  }

  public void deleteRelationsSetJoinsRelationship(er.extensions.eof.ERXGenericRecord object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _OsmRelationSet.RELATIONS_SET_JOINS_KEY);
  }

  public void deleteAllRelationsSetJoinsRelationships() {
    Enumeration<er.extensions.eof.ERXGenericRecord> objects = relationsSetJoins().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteRelationsSetJoinsRelationship(objects.nextElement());
    }
  }


  public static OsmRelationSet createOsmRelationSet(EOEditingContext editingContext, String name
) {
    OsmRelationSet eo = (OsmRelationSet) EOUtilities.createAndInsertInstance(editingContext, _OsmRelationSet.ENTITY_NAME);
    eo.setName(name);
    return eo;
  }

  public static ERXFetchSpecification<OsmRelationSet> fetchSpec() {
    return new ERXFetchSpecification<OsmRelationSet>(_OsmRelationSet.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<OsmRelationSet> fetchAllOsmRelationSets(EOEditingContext editingContext) {
    return _OsmRelationSet.fetchAllOsmRelationSets(editingContext, null);
  }

  public static NSArray<OsmRelationSet> fetchAllOsmRelationSets(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _OsmRelationSet.fetchOsmRelationSets(editingContext, null, sortOrderings);
  }

  public static NSArray<OsmRelationSet> fetchOsmRelationSets(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<OsmRelationSet> fetchSpec = new ERXFetchSpecification<OsmRelationSet>(_OsmRelationSet.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<OsmRelationSet> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static OsmRelationSet fetchOsmRelationSet(EOEditingContext editingContext, String keyName, Object value) {
    return _OsmRelationSet.fetchOsmRelationSet(editingContext, ERXQ.equals(keyName, value));
  }

  public static OsmRelationSet fetchOsmRelationSet(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<OsmRelationSet> eoObjects = _OsmRelationSet.fetchOsmRelationSets(editingContext, qualifier, null);
    OsmRelationSet eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one OsmRelationSet that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OsmRelationSet fetchRequiredOsmRelationSet(EOEditingContext editingContext, String keyName, Object value) {
    return _OsmRelationSet.fetchRequiredOsmRelationSet(editingContext, ERXQ.equals(keyName, value));
  }

  public static OsmRelationSet fetchRequiredOsmRelationSet(EOEditingContext editingContext, EOQualifier qualifier) {
    OsmRelationSet eoObject = _OsmRelationSet.fetchOsmRelationSet(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no OsmRelationSet that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OsmRelationSet localInstanceIn(EOEditingContext editingContext, OsmRelationSet eo) {
    OsmRelationSet localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
