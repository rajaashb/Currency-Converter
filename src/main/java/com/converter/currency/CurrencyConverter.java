package com.converter.currency;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
	
	private static final String API_KEY = "5de34ccf863eb9f7c511009f9860629e";
	private static final String BASE_URL = "https://api.currencylayer.com/convert";
	
	//method to construct API URL
	private static String constructApiUrl(String fromCurrency, String toCurrency, double amount) {
		return BASE_URL + "?access_key=" + API_KEY + "&from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount;
	}

	public static void main(String[] args) throws IOException {
		//currency and amount to convert
		Scanner scanner = new Scanner(System.in);
		String fromCurrency, toCurrency;
		double amount;
		
		while(true) {
			try {
				//getting data from user
				System.out.println("Enter base currency (e.g., USD): ");
				fromCurrency = scanner.nextLine().trim().toUpperCase();
				
				System.out.println("Enter target currency (e.g., LBP): ");
				toCurrency = scanner.nextLine().trim().toUpperCase();
				
				System.out.println("Enter amount to convert: ");
				amount = Double.parseDouble(scanner.nextLine().trim());
				
				//construct API URL
				String apiUrl = constructApiUrl(fromCurrency, toCurrency, amount);
				
				//create HttpURLConnection object
				HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
				connection.setRequestMethod("GET");
				
				//read API response
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String response = reader.readLine();
				
				//parse JSON response
				JSONObject jsonResponse = new JSONObject(response);
				
				if(!jsonResponse.getBoolean("success")) {
					System.out.println("API Error: " + jsonResponse.getJSONObject("error").getString("info"));
				} //checking for success in API response
				
				double result = jsonResponse.getDouble("result");
				
				//print the result
				System.out.println(amount + " " + fromCurrency + " = "+ result + " " + toCurrency);
				break;
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid number format. Please enter a valid amount");
			}
			catch (IOException e) {
				System.out.println("Network error: " + e.getMessage());
			}
			catch (Exception e) {
				System.out.println("Something went wrong: "+ e.getMessage());
			}
		}
		
		scanner.close();
	}

}
