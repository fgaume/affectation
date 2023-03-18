package org.gaume.affectation.io;

import lombok.Data;

@Data
public class FieldsItem{
	private String sqlType;
	private Object defaultValue;
	private Object domain;
	private String name;
	private int length;
	private String alias;
	private String type;
}