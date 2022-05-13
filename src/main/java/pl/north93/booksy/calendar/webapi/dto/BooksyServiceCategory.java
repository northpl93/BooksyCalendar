package pl.north93.booksy.calendar.webapi.dto;

import java.util.List;

public record BooksyServiceCategory(String name,
                                    List<BooksyService> services)
{
}
