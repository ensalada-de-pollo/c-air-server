package com.example.cnuairline.common;

import com.example.cnuairline.reserve.domain.Reserve;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class PdfFormUtil { // pdf 입력을 위한 유틸 클래스

  public static String fillPdfForm(Reserve reserve) throws IOException {
    File file = new File("src/main/pdf/form/template.pdf");
    Map<String, String> fieldValues = setFieldValues(reserve);

    try (PDDocument document = PDDocument.load(file)) {
      PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
      if (acroForm != null) {
        for (PDField field : acroForm.getFieldTree()) {
          String fieldName = field.getFullyQualifiedName();
          if (fieldValues.containsKey(fieldName)) {
            field.setValue(fieldValues.get(fieldName));
          }
        }
        acroForm.setNeedAppearances(false);
        acroForm.flatten();
      }
      String fileName = "e-ticket_" + UUID.randomUUID() + ".pdf";
      Path savePath = Paths.get("src/main/pdf/result", fileName);
      document.save(savePath.toString());
      return fileName;
    }
  }

  private static Map<String, String> setFieldValues(Reserve reserve) {
    Map<String, String> fieldValues = new HashMap<>();
    LocalDateTime departure = reserve.getSeat().getAirplane().getDepartureDateTime();
    LocalDateTime arrival = reserve.getSeat().getAirplane().getArrivalDateTime();

    fieldValues.put("name",
      String.format("%s (%s)", reserve.getCustomer().getName(), reserve.getCustomer().getCno()));
    fieldValues.put("ticketNumber", reserve.getTicketId().toString());
    fieldValues.put("reservationId", reserve.getId().toString());
    fieldValues.put("departureAirport", reserve.getSeat().getAirplane().getDepartureAirport());
    fieldValues.put("arrivalAirport", reserve.getSeat().getAirplane().getArrivalAirport());
    fieldValues.put("text_10kzlt",
      String.format("%s  %s", dateParser(departure), timeParser(departure)));
    fieldValues.put("text_11ipew",
      String.format("%s  %s", dateParser(arrival), timeParser(arrival)));
    fieldValues.put("duration", getDuration(departure, arrival));
    fieldValues.put("flightNumber", reserve.getSeat().getAirplane().getFlightNumber());
    fieldValues.put("airline", reserve.getSeat().getAirplane().getAirline());
    fieldValues.put("seatClass", reserve.getSeat().getSeatClass().toString());
    fieldValues.put("status", reserve.getStatus().toString());
    fieldValues.put("tax", "KRW " + (int) (reserve.getPayment() * 0.1));
    fieldValues.put("total", "KRW " + reserve.getPayment());
    fieldValues.put("reservationDate", dateParser(reserve.getReserveDateTime()));
    fieldValues.put("fuelSurcharge", "-");

    return fieldValues;
  }

  private static String dateParser(LocalDateTime localDateTime) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMMyyyy", Locale.ENGLISH);
    return localDateTime.format(dateTimeFormatter);
  }

  private static String timeParser(LocalDateTime localDateTime) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    return localDateTime.format(dateTimeFormatter);
  }

  private static String getDuration(LocalDateTime departure, LocalDateTime arrival) {
    Duration duration = Duration.between(departure, arrival);

    long total = duration.toMinutes();

    return String.format("%dH %dM", total / 60, total % 60);
  }
}
