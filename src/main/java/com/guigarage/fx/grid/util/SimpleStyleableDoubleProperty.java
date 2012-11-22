package com.guigarage.fx.grid.util;

import com.sun.javafx.css.StyleableDoubleProperty;
import com.sun.javafx.css.StyleableProperty;

public class SimpleStyleableDoubleProperty extends StyleableDoubleProperty {

	private Object bean;
	private String name;
	@SuppressWarnings("rawtypes")
	private StyleableProperty property;
	
	@SuppressWarnings("rawtypes")
	public SimpleStyleableDoubleProperty(Object bean, String name, StyleableProperty property) {
		this.bean = bean;
		this.name = name;
		this.property = property;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public StyleableProperty getStyleableProperty() {
		return property;
	}

	@Override
	public Object getBean() {
		return bean;
	}

	@Override
	public String getName() {
		return name;
	}

}
