package mapper;

import dto.DocumentDto;
import dto.DocumentStatus;
import dto.StatusDto;
import entity.Document;
import lombok.RequiredArgsConstructor;

/**
 * DocumentMapperDecorator.
 *
 * @author Roman_Erzhukov
 */
@RequiredArgsConstructor
public abstract class DocumentMapperDecorator implements DocumentMapper {

    private final DocumentMapper delegate;

    @Override
    public DocumentDto toDto(Document entity) {
        DocumentDto dto = delegate.toDto(entity);
        if (dto == null) {
            return null;
        }
        return dto.setStatus(toStatusDto(entity.getStatus()));
    }

    StatusDto toStatusDto(DocumentStatus status) {
        if (status == null) {
            return null;
        }
        return new StatusDto()
                .setSystem(status)
                .setName(statusName(status));
    }

    private String statusName(DocumentStatus status) {
        switch (status) {
            case NEW:
                return "Новый";
            case DRAFT:
                return "Черновик";
            case SIGNED:
                return "Подписан";
            default:
                return "Исполнен";
        }
    }
}
