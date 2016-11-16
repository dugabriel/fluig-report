package com.fluig.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.fluig.rest.Tools;

public class ServiceApplication extends Application  {
	private Set<Object> singletons = new HashSet<Object>();

	public ServiceApplication() {
		singletons.add(new Tools());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
