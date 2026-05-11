package io.github.tessla2.libraryapi.controller.dto.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleBookResponse(
        int totalItems,
        List<Volume> items
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Volume(
            String id,
            VolumeInfo volumeInfo
    ) {

        public record VolumeInfo(
                String title,
                String[] authors,
                String publisher,
                String publishedDate,
                String description,
                List<String> categories,
                ImageLinks imageLinks,
                List<IndustryIdentifier> industryIdentifiers
        ) {}

        public record IndustryIdentifier(
                String type,
                String identifier
        ) {}

        public record ImageLinks(
                String thumbnail
        ) {}
    }
}
