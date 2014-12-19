package uk.co.linek.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.linek.domain.SampleEntity

@Service
@Transactional
class SampleService {

	public loadSamples() {
		SampleEntity.all
	}

	public saveSample(SampleEntity entity) {
		entity.save failOnError: true
	}
}
