package mapper;

import dto.DocumentDto;
import dto.DocumentStatus;
import dto.StatusDto;
import entity.Document;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * DocumentMapperDecorator.
 *
 * @author Roman_Erzhukov
 */
public abstract class DocumentMapperDecorator implements DocumentMapper {

    @Setter
    @Qualifier("delegate")
    @Autowired
    private DocumentMapper delegate;

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

    @Override
    public Document toEntity(DocumentDto dto) {
        return delegate.toEntity(dto);
    }
}
