package uk.co.linek.batch

import org.springframework.batch.item.ItemWriter

class SampleWriter implements ItemWriter<Map<String, Object>>{

	@Override
	void write(List<? extends Map<String, Object>> items) throws Exception {
		items?.each {item ->
			println item.key + ":" + item.value
		}
	}
}
