package pl.north93.booksy.calendar.webapi.dto;

import java.util.List;

public record BooksyService(String name, List<BooksyServiceVariant> variants)
{
}
