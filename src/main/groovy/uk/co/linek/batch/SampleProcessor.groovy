package uk.co.linek.batch

import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import uk.co.linek.domain.SampleEntity
import uk.co.linek.service.SampleService

class SampleProcessor implements ItemProcessor<SampleEntity, Map<String, Object>> {

	@Autowired
	SampleService sampleService

	@Override
	Map<String, Object> process(SampleEntity entity) throws Exception {
		return [name:entity.name, value:entity.value]
	}
}
