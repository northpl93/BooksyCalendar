package pl.north93.booksy.calendar.place;

public record PlaceDto(String id, String name)
{
    @Override
    public String toString()
    {
        return this.name;
    }
}
