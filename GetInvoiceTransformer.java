package com.au.invoice.transformer;

import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.au.invoice.model.GetInvoiceRequest;
import com.au.invoice.model.GetInvoiceResponse;
import com.au.invoice.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Depak Java Transformer Class to get the payload and generate JSON
 *         Response.
 *
 */
public class GetInvoiceTransformer extends AbstractMessageTransformer {

	/*
	 * (non-Javadoc) This Method gets the JSON Payload and builds the JSON
	 * Response for get products API.
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		GetInvoiceResponse response = null;

		try {
			String request = message.getPayloadAsString();

			System.out.println("request-----" + request);

			ObjectMapper objectMapper = new ObjectMapper();
			GetInvoiceRequest invoiceRequestObj = objectMapper.readValue(request,new TypeReference<GetInvoiceRequest>() {
					});
			System.out.println("customer type : " + invoiceRequestObj.getCustomerType());
			List<Product> productsList = invoiceRequestObj.getProducts();
			Double totalAmount = 0.0d;

			response = new GetInvoiceResponse();
			// build the response

			for (Product product : productsList) {
				
				Double rate = product.getRate();
				System.out.println("rate ----"+rate);
				String tax = product.getTaxRate().substring(0,product.getTaxRate().indexOf("%"));
				System.out.println("tax ----"+tax);
				Double taxRate = Double.valueOf(tax);
				 
				
				if(rate > 0.0 && taxRate > 0.0){
					totalAmount+= rate + (rate * taxRate/100);
				}

			}
			
			response.setTotalAmount(totalAmount);
			response.setAccountNumber(invoiceRequestObj.getAccountNumber());
			response.setOrderNumber(invoiceRequestObj.getOrderNumber());

			response.setProducts(productsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

}
