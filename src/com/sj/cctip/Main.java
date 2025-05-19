package com.sj.cctip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		String[] fileArr = {
				"강원숙박시설정보_675", 
				"경기숙박시설정보_403", 
				"경남숙박시설정보_355",
				"경북숙박시설정보_383",
				"광주숙박시설정보_53",
				"대구숙박시설정보_71",
				"대전숙박시설정보_18",
				"부산숙박시설정보_158",
				"서울숙박시설정보_522",
				"세종숙박시설정보_5",
				"울산숙박시설정보_24",
				"인천숙박시설정보_130",
				"전남숙박시설정보_339",
				"전북숙박시설정보_315",
				"제주숙박시설정보_245",
				"충남숙박시설정보_234",
				"충북숙박시설정보_68"
			};
		for(int i = 0; i < fileArr.length; i++) {
			Main csvReader = new Main();
			List<List<String>> csvList = csvReader.readCSV(fileArr[i]);
			
			makeFile();
			fileSave(csvList);
		}
	}

	private List<List<String>> readCSV(String fileArr) {
		List<List<String>> csvList = new ArrayList<>();
		String path = "./csv/" + fileArr + ".csv";
		File csv = new File(path);
		
		BufferedReader br = null;
		String line = "";
		StringBuilder sb = null;
		int[] idxArr = {0, 7, 13, 14, 17, 18, 19, 20};
		String tableName = "ACCOMMODATION_TB";
		String insertQuery = "";
		String insertColumn = " (ACCOM_ADDR, ACCOM_SQ, ACCOM_LON, ACCOM_LAT, ACCOM_PHONE, ACCOM_NAME, ACCOM_ZIP_CODE, LOC_ID) ";
		try {
			br = new BufferedReader(new FileReader(csv));
			
//			int idx = 0;
			while((line = br.readLine()) != null) {
				sb = new StringBuilder();
				List<String> l = new ArrayList<>();
				String[] strArr = line.split(",");


				for (int i = 0; i < idxArr.length; i++) {
				    String value = strArr[idxArr[i]].trim()
				        .replaceAll("^\"|\"$", "")    // 맨 앞과 맨 뒤의 " 제거
				        .replace("'", "''");          // SQL 홑따옴표 이스케이프

				    if(idxArr[i] == 13 || idxArr[i] == 14 || idxArr[i] == 20) sb.append(value);
				    else sb.append("'" + value + "'");

				    if (i != idxArr.length - 1) sb.append(", ");
				}

//				System.out.println(sb);
				insertQuery = "INSERT INTO " + tableName +  insertColumn + "VALUES(" + sb + ");";
				System.out.println(insertQuery);

				l.add(insertQuery);
				csvList.add(l);
//				idx++;
//				System.out.println(idx + " : " + l);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return csvList;
	}
	
	public static void makeFile() {
		File file = new File("insert.sql");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void fileSave(List<List<String>> csvList) {
	    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("insert.sql", true), "MS949"))) {
	        for (List<String> line : csvList) {
	            for (String query : line) {
	                bw.write(query);
	                bw.newLine(); // 줄바꿈
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
