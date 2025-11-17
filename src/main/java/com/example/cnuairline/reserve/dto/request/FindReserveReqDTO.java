package com.example.cnuairline.reserve.dto.request;

import com.example.cnuairline.reserve.domain.enums.Status;

import java.time.LocalDate;

public record FindReserveReqDTO (Status status, int flag, LocalDate start, LocalDate end) {

}
