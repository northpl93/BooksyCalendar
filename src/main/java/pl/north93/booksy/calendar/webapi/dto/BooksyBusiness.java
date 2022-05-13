package pl.north93.booksy.calendar.webapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BooksyBusiness(String name,
                             @JsonProperty("service_categories")
                             List<BooksyServiceCategory> serviceCategories)
{
}
