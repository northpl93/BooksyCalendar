package pl.north93.booksy.calendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import pl.north93.booksy.calendar.service.dto.ServiceVariantDto;
import pl.north93.booksy.calendar.webapi.dto.BooksyBusiness;
import pl.north93.booksy.calendar.webapi.dto.BooksyService;
import pl.north93.booksy.calendar.webapi.dto.BooksyServiceCategory;
import pl.north93.booksy.calendar.webapi.dto.BooksyServiceVariant;
import pl.north93.booksy.calendar.webapi.service.BooksyClient;

public class ServiceVariantsSupplier implements Supplier<List<ServiceVariantDto>>
{
    private final BooksyClient booksyClient;
    private final String placeId;

    public ServiceVariantsSupplier(final BooksyClient booksyClient, final String placeId)
    {
        this.booksyClient = booksyClient;
        this.placeId = placeId;
    }

    @Override
    public List<ServiceVariantDto> get()
    {
        final List<ServiceVariantDto> variantDtos = new ArrayList<>();

        final BooksyBusiness business = this.booksyClient.getBusiness(this.placeId);
        for (final BooksyServiceCategory serviceCategory : business.serviceCategories())
        {
            for (final BooksyService service : serviceCategory.services())
            {
                for (final BooksyServiceVariant variant : service.variants())
                {
                    final String name = serviceCategory.name() + " - " + service.name() + " - " + variant.label();
                    variantDtos.add(new ServiceVariantDto(name, variant.id()));
                }
            }
        }

        return variantDtos;
    }
}
