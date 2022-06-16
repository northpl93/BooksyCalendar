package pl.north93.booksy.calendar.service.dto;

public record ServiceVariantDto(String name, int serviceVariantId)
{
    @Override
    public String toString()
    {
        return this.name;
    }
}
