package pl.north93.booksy.calendar.service;

import java.util.List;

import pl.north93.booksy.calendar.service.dto.ServiceVariantDto;

public record ServicesVariantsDownloadedEvent(List<ServiceVariantDto> serviceVariantDtos)
{
}