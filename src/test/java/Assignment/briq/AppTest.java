package Assignment.briq;

import org.testng.annotations.Test;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import com.github.opendevl.JFlat;
import com.google.common.io.Files;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.apache.commons.io.FileUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;

/**
 * Unit test for simple App.
 */
public class AppTest {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
	}

	// @Test
	public void site_scraping() throws Exception {

		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + File.separator + "Driver" + File.separator + "chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://www.bizjournals.com/milwaukee/feature/crane-watch");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id=\"v-app\"]/div[3]/div/div/a[1]")).click();
		WebDriverWait wait = new WebDriverWait(driver, 30);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"bx-close-inside-1176467\"]/svg")));
		// driver.findElement(By.xpath("//*[@id=\\\"bx-close-inside-1176467\\\"]/svg")).click();
		Thread.sleep(5000);
		List<WebElement> element = driver.findElements(By.xpath("//*[@id=\"milwaukee_7663_layer\"]/image"));

		System.out.println("Size" + element.size());
	}

	@Test
	public void excel_json() {
		List<Leads> customers = utilities.readExcelFile();
		String jsonString = utilities.convertObjects2JsonString(customers);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss.SSS");
		String filename = System.getProperty("user.dir") + File.separator + "testData" + File.separator + "output"
				+ File.separator + "Leads_" + sdf.format(new Date()) + ".json";
		File myObj = new File(filename);
		try {
			FileWriter myWriter = new FileWriter(myObj);
			myWriter.write(jsonString);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test
	public void api_response() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss.SSS");
		String filename = System.getProperty("user.dir") + File.separator + "testData" + File.separator + "output"
				+ File.separator + "response" + sdf.format(new Date()) + ".xlsx";
		String api_url = "https://data.sfgov.org/resource/p4e4-a5a7.json";
		Response resp = RestAssured.given().when().get(api_url).then().extract().response();

		List<JResponse> response = utilities.convertJsonString2Objects(resp.asString().replace(":@", ""));

		try {
			utilities.writeObjects2ExcelFile(response, filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test
	public void test_readPDF_statement() {

		try {
			// PDFReader

			PdfReader reader = new PdfReader(System.getProperty("user.dir") + File.separator + "testData"
					+ File.separator + "sample statement.PDF");
			PdfDocument pdfDoc = new PdfDocument(reader); // get the number of pages in
			int noOfPages = pdfDoc.getNumberOfPages();
			String pdf = "";
			System.out.println("Extracted content of PDF---- ");
			for (int i = 1; i <= noOfPages; i++) { // Extract content of each page
				String contentOfPage = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i));
				pdf = pdf + contentOfPage;
			}
			pdfDoc.close();
			System.out.println("Customer Name : " + pdf.split("\\r?\\n")[2]);
			System.out.println("Bank Address : " + pdf.split("\\r?\\n")[0] + " " + pdf.split("\\r?\\n")[1]);
			System.out.println("Customer Address : " + pdf.split("\\r?\\n")[3] + " " + pdf.split("\\r?\\n")[4]);
			System.out.println("Account number : " + pdf.split("\\r?\\n")[5].split(": ")[1]);
			System.out.println("Statement date : " + pdf.split("\\r?\\n")[7].split(": ")[1]);
			System.out.println("Ending Balance : "
					+ pdf.split("\\r?\\n")[17].split(" ")[pdf.split("\\r?\\n")[17].split(" ").length - 1]);
			System.out.println("Total Withdrawls : $"
					+ pdf.split("\\r?\\n")[27].split(" ")[pdf.split("\\r?\\n")[27].split(" ").length - 1]);
			System.out.println("total Deposits : "
					+ pdf.split("\\r?\\n")[21].split(" ")[pdf.split("\\r?\\n")[21].split(" ").length - 1]);
			System.out.println("Total Checks : "
					+ pdf.split("\\r?\\n")[33].split(" ")[pdf.split("\\r?\\n")[33].split(" ").length - 1]);
		} catch (Exception e) {
			System.out.println("Exception occurred " + e.getMessage());
			Assert.fail();
		}
	}
	
	@Test
	public void test_readPDF_active_lice() {

		try {
			// PDFReader

			PdfReader reader = new PdfReader(System.getProperty("user.dir") + File.separator + "testData"
					+ File.separator + "active licences.pdf");
			PdfDocument pdfDoc = new PdfDocument(reader); // get the number of pages in
			int noOfPages = pdfDoc.getNumberOfPages();
			
			System.out.println("Extracted content of PDF---- ");
			for (int i = 1; i <= noOfPages; i++) { // Extract content of each page
				String contentOfPage = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i));
			}
			pdfDoc.close();
		} catch (Exception e) {
			System.out.println("Exception occurred " + e.getMessage());
			Assert.fail();
		}
	}

}
