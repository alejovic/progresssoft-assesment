package com.progresssoft.deal.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progresssoft.deal.common.CSVUtil;
import com.progresssoft.deal.entity.dto.DealDTO;

@RunWith(MockitoJUnitRunner.class)
public class CSVGeneratorServiceTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	//@Test
	public void test_generateCSV() {

		String csvFile = "D:\\DEAL_SOURCE_TEST.csv";
		FileWriter writer = null;
		try {
			writer = new FileWriter(csvFile);
		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			CSVUtil.writeLine(writer, Arrays.asList("Deal Unique Id", "From Currency ISO Code Ordering Currency",
					"To Currency ISO Code", "Deal timestamp", "Deal Amount in ordering currency"));
		} catch (IOException e) {
			fail(e.getMessage());
		}

		CSVGeneratorService generatorService = mock(CSVGeneratorService.class);
		when(generatorService.loadData()).thenReturn(loadDefaultList());

		CSVGeneratorImpl generatorImpl = new CSVGeneratorImpl(generatorService);
		List<DealDTO> dealList = generatorImpl.loadDefaultList();

		try {
			for (DealDTO d : dealList) {

				List<String> list = new ArrayList<>();
				list.add(d.getId() + "");
				list.add(d.getIsoSource());
				list.add(d.getIsoTarget());
				list.add(d.getDate().toString());
				list.add(d.getAmount().toString());
				assertEquals(d.getIsoSource().equalsIgnoreCase(d.getIsoTarget()), false);
				
				CSVUtil.writeLine(writer, list);
			}

			assertEquals(true, dealList.size() > 1);

		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}

	private List<DealDTO> loadDefaultList() {
		List<DealDTO> dealList = new ArrayList<>();
		final long seed = 150000;
		Random random = new Random(seed);
		for (int i = 0; i < seed; i++) {
			dealList.add(new DealDTO((i + seed)+"", ISOCodeTest.randomISOCode().toString(),
					ISOCodeTest.randomISOCode().toString(), getDate_YYYYMMddHHmmss(),
					BigDecimal.valueOf(random.nextInt(50000) + random.nextDouble()).setScale(2, RoundingMode.CEILING).toString(), i));
		}
		return dealList;

	}

	private enum ISOCodeTest {
		AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, COP, CLP;
		private static final List<ISOCodeTest> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static ISOCodeTest randomISOCode() {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}

	private String getDate_YYYYMMddHHmmss() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		return sdf.format(calendar.getTime());
	}

}
