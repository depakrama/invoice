package com.au.products.transformer;

import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.au.products.model.GetProductRequest;
import com.au.products.model.GetProductResponse;
import com.au.products.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Depak Java Transformer Class to get the payload and generate JSON
 *         Response.
 *
 */
public class GetProductTransformer extends AbstractMessageTransformer {

	/*
	 * (non-Javadoc) This Method gets the JSON Payload and builds the JSON
	 * Response for get products API.
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		GetProductResponse response = null;

		try {
			String request = message.getPayloadAsString();

			System.out.println("request-----" + request);

			ObjectMapper objectMapper = new ObjectMapper();
			GetProductRequest productRequestObj = objectMapper.readValue(request,new TypeReference<GetProductRequest>() {
					});
			System.out.println("customer type : " + productRequestObj.getCustomerType());
			List<Product> productsList = productRequestObj.getProducts();

			response = new GetProductResponse();
			// build the response
			String customerType = productRequestObj.getCustomerType();

			for (Product product : productsList) {
				String productType = product.getType();
				String productName = product.getName();
				
				System.out.println("product name : " + product.getName());
				
				System.out.println("product type : " + product.getType());

				switch (customerType) {
						case "Employee": {
							switch (productName) {
								case "BroadBand": {
									switch (productType) {
										case "A": {
											product.setRate(0.00d);
											break;
										}
										default:{
											product.setRate(12.00d);
											break;
										}
									}
									break;
								}
								case "HomePhone": {
									product.setRate(12.00d);
									break;
								}
								case "TV": {
									product.setRate(10.00d);
									break;
								}
								default:{
									break;
								}
							}
							break;
						}
	
					case "Vip": {
						switch (productName) {
							case "BroadBand": {
								product.setRate(15.00d);
								break;
							}
							case "HomePhone": {
								product.setRate(0.00d);
								break;
							}
							case "TV": {
								product.setRate(10.00d);
								break;
							}
							default:{
								break;
							}
					}
					break;
					}
					
					case "Resident": {
						switch (productName) {
							case "BroadBand": {
								product.setRate(15.00d);
								break;
							}
							case "HomePhone": {
								product.setRate(12.00d);
								break;
							}
							case "TV": {
								product.setRate(10.00d);
								break;
							}
							default:{
								break;
							}
					}
					break;
					}
					
					default:{
						break;
					}
				}
			}

			response.setProducts(productsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

}
