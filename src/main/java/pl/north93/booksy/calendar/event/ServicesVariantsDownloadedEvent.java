package pl.north93.booksy.calendar.event;

import java.util.List;

import pl.north93.booksy.calendar.dto.ServiceVariantDto;

public record ServicesVariantsDownloadedEvent(List<ServiceVariantDto> serviceVariantDtos)
{
}