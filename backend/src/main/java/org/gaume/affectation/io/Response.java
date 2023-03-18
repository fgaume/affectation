package org.gaume.affectation.io;

import lombok.Data;

import java.util.List;

@Data
public class Response{
	private List<FeaturesItem> features;
	/* private String globalIdFieldName;
	private String objectIdFieldName;
	private SpatialReference spatialReference;
	private List<FieldsItem> fields;
	private UniqueIdField uniqueIdField;
	private String geometryType; */
}