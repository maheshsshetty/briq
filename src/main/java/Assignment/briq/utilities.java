package Assignment.briq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class utilities {
	public static String convertObjects2JsonString(List<Leads> customers) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";

		try {
			jsonString = mapper.writeValueAsString(customers);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	public static List<Leads> readExcelFile() {
		try {
			FileInputStream excelFile = new FileInputStream(new File(
					System.getProperty("user.dir") + File.separator + "testData" + File.separator + "leads.xlsx"));
			Workbook workbook = new XSSFWorkbook(excelFile);

			Sheet sheet = workbook.getSheet("leads");
			Iterator<Row> rows = sheet.iterator();

			List<Leads> lstCustomers = new ArrayList<Leads>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				Leads cust = new Leads();
				DataFormatter formatter = new DataFormatter();
				int cellIndex = 0;
				int flag = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					if (cellIndex == 0) {
						cust.setProjectName(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 1) {
						cust.setProjectType(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 2) {
						cust.setDescription(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 3) {
						cust.setSqft(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 4) {
						cust.setEstimatedProjectCost(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 5) {
						cust.setPermitNumber(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 6) {
						cust.setNoticeType(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 7) {
						cust.setStreet(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 8) {
						cust.setCity(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 9) {
						cust.setState(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 10) {
						cust.setZipcode(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 11) {
						cust.setContactInfo(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 12) {
						cust.setContactPhone(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 13) {
						cust.setContactAddress(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 14) {
						cust.setContactEmail(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 15) {
						cust.setOwner(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 16) {
						cust.setArchitect(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 17) {
						cust.setApplicationDate(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 18) {
						cust.setUploadDate(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 19) {
						cust.setStatus(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 20) {
						cust.setCloseDate(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 21) {
						cust.setLink(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 22) {
						cust.setSource(formatter.formatCellValue(currentCell));
					} else if (cellIndex == 23) {
						cust.setConstructionStartDate(formatter.formatCellValue(currentCell));
					}

					cellIndex++;
				}

				lstCustomers.add(cust);
			}

			// Close WorkBook
			workbook.close();

			return lstCustomers;
		} catch (IOException e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	
	public static void writeObjects2ExcelFile(List<JResponse> customers, String filePath) throws IOException {
		String[] COLUMNs = { "computedRegionRxqgMtj9", "location_latitude", "location_human_address",
				"location_needs_recoding", "location_longitude", "filedDate", "recordId", "zipcode", "street_number",
				"computed_region_ajp5_b2md", "computed_region_yftq_j783", "computed_region_bh8s_q3mv",
				"computed_region_uruc_drv6", "block", "computed_region_jx4q_fizf", "permit_type_definition",
				"computed_region_qgnn_b9vv", "neighborhoods_analysis_boundaries", "supervisor_district", "description",
				"revised_cost", "permit_creation_date", "street_name", "street_suffix", "permit_number", "status_date",
				"status", "computed_region_26cr_cadq", "lot", "computed_region_6qbp_sg9q", "permit_type" };

		Workbook workbook = new XSSFWorkbook();

		CreationHelper createHelper = workbook.getCreationHelper();

		Sheet sheet = workbook.createSheet("Response1");

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		Row headerRow = sheet.createRow(0);

		// Header
		for (int col = 0; col < COLUMNs.length; col++) {
			Cell cell = headerRow.createCell(col);
			cell.setCellValue(COLUMNs[col]);
			cell.setCellStyle(headerCellStyle);
		}

		CellStyle ageCellStyle = workbook.createCellStyle();
		ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

		int rowIdx = 1;
		for (JResponse customer : customers) {
			Row row = sheet.createRow(rowIdx++);

			row.createCell(0).setCellValue(customer.getComputed_region_rxqg_mtj9());

			if (customer.getLocation() != null && customer.getLocation().getLatitude() != null
					&& customer.getLocation().getLatitude() != null
					&& customer.getLocation().getNeeds_Recoding() != null
					&& customer.getLocation().getLongitude() != null) {
				row.createCell(1).setCellValue(customer.getLocation().getLatitude());
				row.createCell(2).setCellValue(customer.getLocation().getHuman_address());
				row.createCell(3).setCellValue(customer.getLocation().getNeeds_Recoding());
				row.createCell(4).setCellValue(customer.getLocation().getLongitude());
			}
			row.createCell(5).setCellValue(customer.getFiled_date());
			row.createCell(6).setCellValue(customer.getRecord_id());
			row.createCell(7).setCellValue(customer.getZipcode());
			row.createCell(8).setCellValue(customer.getStreet_number());
			row.createCell(9).setCellValue(customer.getComputed_region_ajp5_b2md());
			row.createCell(10).setCellValue(customer.getComputed_region_yftq_j783());
			row.createCell(11).setCellValue(customer.getComputed_region_bh8s_q3mv());
			row.createCell(12).setCellValue(customer.getComputed_region_uruc_drv6());
			row.createCell(13).setCellValue(customer.getNeighborhoods_analysis_boundaries());
			row.createCell(14).setCellValue(customer.getSupervisor_district());
			row.createCell(15).setCellValue(customer.getDescription());
			row.createCell(16).setCellValue(customer.getRevised_cost());
			row.createCell(17).setCellValue(customer.getPermit_creation_date());
			row.createCell(18).setCellValue(customer.getStreet_name());
			row.createCell(19).setCellValue(customer.getStreet_suffix());
			row.createCell(20).setCellValue(customer.getPermit_number());
			row.createCell(21).setCellValue(customer.getStatus_date());
			row.createCell(22).setCellValue(customer.getStatus());
			row.createCell(23).setCellValue(customer.getComputed_region_26cr_cadq());
			row.createCell(24).setCellValue(customer.getLot());
			row.createCell(25).setCellValue(customer.getComputed_region_6qbp_sg9q());
			row.createCell(26).setCellValue(customer.getPermitType());

		}

		FileOutputStream fileOut = new FileOutputStream(filePath);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	public static List<JResponse> convertJsonString2Objects(String jsonString) {
		List<JResponse> resp = null;

		try {
			resp = new ObjectMapper().readValue(jsonString, new TypeReference<List<JResponse>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resp;
	}
}
