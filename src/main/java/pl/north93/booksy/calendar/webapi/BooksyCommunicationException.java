package pl.north93.booksy.calendar.webapi;

public class BooksyCommunicationException extends RuntimeException
{
    public BooksyCommunicationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
