package model.services;

import java.time.Duration;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {
	
	private Double pricePerHour;
	private Double pricePerDay;
	
	private TaxService taxService;
	
	// sem contructor sem algumentos para obrigar a informar os dados quanto instanciar o RentalService. 
	
	public RentalService(Double pricePerHour, Double pricePerDay, TaxService taxService) {
		this.pricePerHour = pricePerHour;
		this.pricePerDay = pricePerDay;
		this.taxService = taxService;
	}
	
	// lógica para jogar a fatura dentro do CarRental.
	public void processInvoice(CarRental carRental) {
		
		// para encontrar a duração entre dois instantes em minutos. (ex. 250min de tempo).
		double minutes = Duration.between(carRental.getStart(), carRental.getFinish()).toMinutes();
		double hours = minutes/60.0; // calcula a fração
		
		double basicPayment;
		if (hours <= 12.0) {
			basicPayment = pricePerHour * Math.ceil(hours); // Math.ceil() para arredondar para cima (teto do valor).
		} else {
			basicPayment = pricePerDay * Math.ceil(hours / 24.0); // para pegar em dias
		}
		
		double tax = taxService.tax(basicPayment);
		
		carRental.setInvoice(new Invoice(basicPayment, tax));
	}
}
