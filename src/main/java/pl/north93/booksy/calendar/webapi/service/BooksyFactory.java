package pl.north93.booksy.calendar.webapi.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import pl.north93.booksy.calendar.webapi.BooksyCommunicationException;

public final class BooksyFactory
{
    public static BooksyClient createBooksyClient(final HttpClient httpClient)
    {
        final ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());

        return new BooksyClient(httpClient, objectMapper, obtainApiKey(httpClient));
    }

    private static String obtainApiKey(final HttpClient httpClient)
    {
        try
        {
            final HttpEntity entity = httpClient.execute(new HttpGet("https://booksy.com/pl-pl/")).getEntity();
            final String body = new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8);

            final int apiKeyIndex = body.indexOf("apiKey:") + 8;
            final int closingTag = body.indexOf('"', apiKeyIndex);

            return body.substring(apiKeyIndex, closingTag);
        }
        catch (final IOException exception)
        {
            throw new BooksyCommunicationException("Failed to obtain API key", exception);
        }
    }
}
