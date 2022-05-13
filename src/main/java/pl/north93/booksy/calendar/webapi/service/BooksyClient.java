package pl.north93.booksy.calendar.webapi.service;

import static java.text.MessageFormat.format;


import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;

import pl.north93.booksy.calendar.webapi.BooksyCommunicationException;
import pl.north93.booksy.calendar.webapi.dto.BooksyBusiness;
import pl.north93.booksy.calendar.webapi.dto.BooksyBusinessResponse;
import pl.north93.booksy.calendar.webapi.dto.BooksyTimeSlotsResponse;

public class BooksyClient
{
    private static final String BOOKSY_API_PREFIX = "https://pl.booksy.com/api/pl/2/customer_api/";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public BooksyClient(final HttpClient httpClient, final ObjectMapper objectMapper, final String apiKey)
    {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public BooksyBusiness getBusiness(final String businessId)
    {
        final String url = format("businesses/{0}/?no_thumbs=true", businessId);
        return this.getForEntity(url, BooksyBusinessResponse.class).business();
    }

    public BooksyTimeSlotsResponse getTimeSlots(final int serviceVariantId, final LocalDate startDate, final LocalDate endDate)
    {
        final String url = format("me/bookings/time_slots?service_variant_id={0,number,#}&start_date={1}&end_date={2}", serviceVariantId, startDate, endDate);
        return this.getForEntity(url, BooksyTimeSlotsResponse.class);
    }

    private <T> T getForEntity(final String url, final Class<T> entityClass)
    {
        try
        {
            final HttpGet request = new HttpGet(BOOKSY_API_PREFIX + url);
            request.setHeader(new BasicHeader("x-api-key", this.apiKey));

            final HttpResponse httpResponse = this.httpClient.execute(request);
            return this.objectMapper.readValue(new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8), entityClass);
        }
        catch (final IOException e)
        {
            throw new BooksyCommunicationException("Failed to getEntity " + url, e);
        }
    }
}
