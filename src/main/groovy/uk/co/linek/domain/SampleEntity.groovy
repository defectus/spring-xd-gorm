package uk.co.linek.domain

import grails.persistence.Entity

@Entity
class SampleEntity {
	String name
	String value

	@Override
	int hashCode() {
		name.hashCode() + value.hashCode()
	}

	@Override
	boolean equals(Object obj) {
		if (!obj || !(obj instanceof  SampleEntity))
			false
		name?.equals(obj?.name) && value?.equals(obj?.value)
	}
	static transients = ['errors', 'instanceGormInstanceApi', 'instanceGormValidationApi', 'staticGormStaticApi', 'instanceConvertersApi', 'attached', 'dirty']
}
