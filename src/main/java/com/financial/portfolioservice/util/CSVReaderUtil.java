package com.financial.portfolioservice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.financial.portfolioservice.beans.Stock;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

@Component
public class CSVReaderUtil {

	public List<String[]> readAll(Reader reader) throws Exception {
		CSVReader csvReader = new CSVReader(reader);
		List<String[]> list = new ArrayList<>();
		list = csvReader.readAll();
		reader.close();
		csvReader.close();
		return list;
	}

	public List<Stock> csvToBean() throws IllegalStateException, Exception {
		ColumnPositionMappingStrategy<Stock> strat = new ColumnPositionMappingStrategyBuilder<Stock>().build();
		strat.setType(Stock.class);

		String[] columns = new String[] { "quote", "weightage", "currentPrice" };
		strat.setColumnMapping(columns);

		return new CsvToBeanBuilder<Stock>(new CSVReader(loadCSV())).withType(Stock.class).build().parse();
	}

	public List<String[]> loadStocksFromCSV() throws Exception {
		return readAll(loadCSV());
	}

	public Reader loadCSV() throws Exception {
		File file = ResourceUtils.getFile("classpath:nifty_50_data.csv");
		FileReader csvFile = new FileReader(file);
		return new BufferedReader(csvFile);
	}

}
