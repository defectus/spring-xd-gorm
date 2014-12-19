package uk.co.linek.batch

import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.NonTransientResourceException
import org.springframework.batch.item.ParseException
import org.springframework.batch.item.UnexpectedInputException
import org.springframework.beans.factory.annotation.Autowired
import uk.co.linek.domain.SampleEntity
import uk.co.linek.service.SampleService

class SampleReader implements ItemReader<SampleEntity> {

	@Autowired
	SampleService sampleService

	Iterator<SampleEntity> sampleEntityIterator

	@Override
	SampleEntity read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		if (sampleEntityIterator == null) {
			synchronized (this) {
				if (!sampleEntityIterator) {
					sampleEntityIterator = getAllEntities().iterator()
				}
			}
		}
		synchronized (this) {
			return sampleEntityIterator.hasNext() ? sampleEntityIterator.next() : null
		}
	}

	private List getAllEntities() {
		return sampleService.loadSamples()
	}
}
