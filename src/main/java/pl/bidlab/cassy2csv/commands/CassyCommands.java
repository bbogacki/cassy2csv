package pl.bidlab.cassy2csv.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVWriter;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

@Component
public class CassyCommands implements CommandMarker {
	private Cluster cluster;
	private Session session;
	private char separator = ';';
	private String directory = ".";

	public void close() {
		cluster.close();
	}

	@CliAvailabilityIndicator({ "query" })
	public boolean isQueryAvailable() {
		return cluster != null && session != null;
	}

	@CliCommand(value = "set", help = "Sets Cassy2Csv custom properties")
	public String set(
			@CliOption(key = { "separator" }, mandatory = false, help = "CSV separator, default: ';'") String separator,
			@CliOption(key = { "directory" }, mandatory = false, help = "Output directory, default: './'") String directory
			) {
		
		if (separator != null)
		{
			this.separator = separator.charAt(0);
		}
		
		if (directory != null)
		{
			this.directory = directory;
		}
		
		return showCassyVariables();
	}
	
	private String showCassyVariables() {
		return "separator = " + separator + "\n" +
				"directory = " + directory;
	}

	@CliCommand(value = "connect", help = "Connects to Cassandra database")
	public String connect(
			@CliOption(key = { "node" }, mandatory = true, help = "Cassandra's cluster IP address") String node,
			@CliOption(key = { "keyspace" }, mandatory = false, help = "Cassandra's keyspace") String keyspace) {
		
		cluster = Cluster.builder().addContactPoint(node).build();

		Metadata metadata = cluster.getMetadata();

		session = cluster.connect();
		
		if (keyspace != null) {
			session.execute("USE " + keyspace + ";");
		}

		return "Connected to cluster: " + metadata.getClusterName() + "\n"
				+ "Keyspace: " + session.getLoggedKeyspace();
	}

	@CliCommand(value = "query", help = "Queries database and saves output to CSV file")
	public String query(
			@CliOption(key = { "stmt" }, mandatory = true, help = "CQL query statement") final String q,
			@CliOption(key = { "use" }, mandatory = false, help = "Specify Cassandra's keyspace to use") final String keyspace,
			@CliOption(key = { "filename" }, mandatory = false, help = "Specify output filename") final String filename
			) {
		
		String outputPath = directory + File.separator;
		Long savedCounter = 0l;
		
		if (filename != null)
		{
			outputPath += filename; 
		}
		else
		{
			outputPath += "result_" + new Date().getTime() + ".csv";
		}

		if (keyspace != null) {
			session.execute("USE " + keyspace + ";");
		}

		ResultSet rs = session
				.execute(q);

		List<String> headers = new ArrayList<String>();
		List<DataType> types = new ArrayList<DataType>();

		for (Definition d : rs.getColumnDefinitions()) {
			headers.add(d.getName());
			types.add(d.getType());
		}

		String[] headersArray = headers.toArray(new String[headers.size()]);
		String[] valuesArray;

		FileWriter fw;
		try {
			fw = new FileWriter(outputPath);
			CSVWriter writer = new CSVWriter(fw, separator);
			writer.writeNext(headersArray);
			savedCounter = 1l;

			for (Row row : rs.all()) {
				valuesArray = new String[headers.size()];
				for (int i = 0; i < headers.size(); i++) {
					valuesArray[i] = testAndGet(row, i, types.get(i));
				}
				writer.writeNext(valuesArray);
				savedCounter += 1l;
			}
			
			writer.close();
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			return errors.toString();
		}

		return "Success, " + savedCounter + " rows saved to " + outputPath;
	}

	private String testAndGet(Row row, int i, DataType dataType) {
		try {
			return dataType.deserialize(row.getBytesUnsafe(i)).toString();
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}
}